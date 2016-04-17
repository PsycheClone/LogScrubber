package org.singular.scrubber;

import com.jcraft.jsch.JSchException;
import org.singular.connect.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class Scrubber implements BeanFactoryAware {

    @Value("${scrub}")
    private boolean scrub;

    @Value("${hosts.esb}")
    private String[] esbHosts;

    @Value("${location.esb}")
    private String esbLogLocation;

    @Value("${hosts.mdp}")
    private String[] mdpHosts;

    @Value("${location.mdp}")
    private String mdpLogLocation;

    @Value("${melexis.username}")
    private String user;

    @Value("${melexis.password}")
    private String password;

    private BeanFactory beanFactory;

    private Logger LOGGER = LoggerFactory.getLogger(Scrubber.class);

    private final static Map<String, String> ENVIRONMENTS = new HashMap();

    private final static String rootDir = System.getProperty("user.dir") + "/logs/";

    @PostConstruct
    public void start() {
        if(scrub) {
            for (String site : Arrays.asList(esbHosts)) {
                ENVIRONMENTS.put(site, esbLogLocation);
            }
            for (String site : Arrays.asList(mdpHosts)) {
                ENVIRONMENTS.put(site, mdpLogLocation);
            }
            LOGGER.info("Starting LogDownloader.");
            for (final Map.Entry<String, String> environment : ENVIRONMENTS.entrySet()) {
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
    }

    public void retrieveLogs(String host, String path) throws IOException, JSchException {
        Connector connector = new Connector();
        Slicer slicer = (Slicer) beanFactory.getBean("slicer");
        slicer.setHost(host);
        BufferedReader br = connector.connect(host, user, password).readFileBackwards(path);
        String line;

        LOGGER.info("Starting to read from " + host);
        while ((line = br.readLine()) != null) {
            LOGGER.debug(line);
            slicer.process(line);
        }
        LOGGER.info("Connector for " + host + " found no more logs, disconnecting...");
        br.close();
        connector.disconnect();
        LOGGER.info("Connector for " + host + " disconnected.");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
