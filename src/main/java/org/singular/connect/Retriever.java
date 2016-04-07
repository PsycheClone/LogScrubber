package org.singular.connect;

import com.jcraft.jsch.JSchException;
import org.singular.config.SitesConfig;
import org.singular.fileManager.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class Retriever {

    @Autowired
    private FileManager fileManager;

    @Autowired
    private SitesConfig sitesConfig;

    @Value("${melexis.username}")
    private String user;

    @Value("${melexis.password}")
    private String password;

    private Logger LOGGER = LoggerFactory.getLogger(Retriever.class);

    private final static Map<String, String> ENVIRONMENTS = new HashMap();

    private final static String esbPath = "/usr/share/apache-servicemix/data/log/perf4j.log";

    private final static String rootDir = System.getProperty("user.dir") + "/logs/";
    private final static String logsDir = rootDir + "downloaded/";

    @PostConstruct
    public void start() {
        for(String site : sitesConfig.getSites()) {
            ENVIRONMENTS.put(site, esbPath);
        }
        LOGGER.info("Starting LogDownloader.");
        for(final Map.Entry<String, String> environment : ENVIRONMENTS.entrySet()) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        LOGGER.info("Retrieving logs from " + environment.getKey());
                        retrieveLogs(environment.getKey(), environment.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSchException e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    public void retrieveLogs(String host, String path) throws IOException, JSchException {
        Connector connector = new Connector();
        BufferedReader br = connector.connect(host, user, password).getStreamTail1000(path);
        File folderName = fileManager.createFolderIfNotExists(logsDir);
        String fileName = createFileName(host);
        PrintWriter writer = new PrintWriter(folderName + "/" + fileName, "UTF-8");
        String line;
        LOGGER.info("Writing to file " + fileName);
        while ((line = br.readLine()) != null) {
            writer.write(line);
            writer.write("\n");
        }
        writer.close();
        connector.disconnect();
        LOGGER.info("Connector for " + host + " disconnected");
        markFileAsDone(folderName, host);
    }

    private void markFileAsDone(File folderName, String host) {
        File file = new File(folderName + "/" + createFileName(host));
        File destination = fileManager.createFolderIfNotExists(logsDir);
        File renamedFile = new File(destination + "/" + createDoneFileName(host));
        file.renameTo(renamedFile);
        LOGGER.info(createFileName(host) + " file downloaded, renamed to " + createDoneFileName(host));
    }

    private String createFileName(String host) {
        return host + ".log";
    }

    private String createDoneFileName(String host) {
        return host + "-done.log";
    }
}
