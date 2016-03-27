package org.singular.connect;

import com.jcraft.jsch.JSchException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class LogDownloader {
    private final static Map<String, String> ENVIRONMENTS = new HashMap();
    private final static String esbPath = "/usr/share/apache-servicemix/data/log/perf4j.log";
    private final static String rootDir = System.getProperty("user.dir");

    private Connecter connecter = new Connecter();

    public LogDownloader() {
        ENVIRONMENTS.put("esb-a-test.erfurt.elex.be", esbPath);
        ENVIRONMENTS.put("esb-a-test.sofia.elex.be", esbPath);
    }

    public void start() {
        for(final Map.Entry<String, String> environment : ENVIRONMENTS.entrySet()) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        retrieveLogs(environment.getKey(), environment.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSchException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("done");
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    public void retrieveLogs(String host, String path) throws IOException, JSchException {
        BufferedReader br = connecter.connect(host).getStream(path);
        String fileName = createFile(host);
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        String line;
        while ((line = br.readLine()) != null) {
            writer.write(line);
            writer.write("\n");
        }
        connecter.disconnect();
    }

    private String createFolderIfNotExists() {
        File folder = new File(rootDir+"/logs");
        if(!folder.exists()) {
            folder.mkdir();
        }
        return folder.getAbsolutePath();
    }

    private String createFile(String host) {
        String fileName = createFolderIfNotExists();
        new File(fileName);
        return fileName+"/"+host+".log";
    }
}
