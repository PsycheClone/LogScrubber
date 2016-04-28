package org.singular.scrubber;

import com.jcraft.jsch.JSchException;
import org.singular.config.Locations;
import org.singular.connect.LocalConnector;
import org.singular.connect.RemoteConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class Scrubber implements BeanFactoryAware {

    @Value("${scrub}")
    private boolean scrub;

    @Value("${melexis.username}")
    private String user;

    @Value("${melexis.password}")
    private String password;

    @Autowired
    private Locations locations;

    private BeanFactory beanFactory;

    private Logger LOGGER = LoggerFactory.getLogger(Scrubber.class);

    private final static Map<String, String> ENVIRONMENTS = new HashMap();

    @PostConstruct
    public void start() {
        if(scrub) {
            fillEnvironments();
            LOGGER.info("Starting Scrubber.");
            for (final Map.Entry<String, String> environment : ENVIRONMENTS.entrySet()) {
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            LOGGER.info("Retrieving logs from " + environment.getKey());
                            if(environment.getKey().equalsIgnoreCase("localhost")) {
                                retrieveLocalLogs(environment.getKey(), environment.getValue());
                            } else {
                                retrieveRemoteLogs(environment.getKey(), environment.getValue());
                            }
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

    private void retrieveRemoteLogs(String host, String path) throws IOException, JSchException {
        RemoteConnector connector = new RemoteConnector();
        BufferedReader reader = connector.connect(host, user, password).readFileBackwards(path);
        sliceLogs(reader, host);
    }

    private void retrieveLocalLogs(String name, String path) throws IOException {
        LocalConnector connector = new LocalConnector();
        BufferedReader reader = connector.readFileBackwards(path);
        sliceLogs(reader, name);
    }

    private void sliceLogs(BufferedReader reader, String name) throws IOException {
        Slicer slicer = (Slicer) beanFactory.getBean("slicer");
        slicer.setHost(name);
        String line;

        LOGGER.info("Starting to read from " + name);
        while ((line = reader.readLine()) != null) {
            LOGGER.debug(line);
            slicer.process(line);
        }
        LOGGER.info("Connector for " + name + " found no more logs, disconnecting...");
        reader.close();
        LOGGER.info("Connector for " + name + " disconnected.");
    }

    private void fillEnvironments() {
        for (String site : locations.getLocations()) {
            String host = site.substring(0, site.indexOf("/"));
            String path = site.substring(site.indexOf("/"));
            ENVIRONMENTS.put(host, path);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
