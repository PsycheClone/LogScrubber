package org.singular.scrubber;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.singular.config.Locations;
import org.singular.connect.local.FileTailer;
import org.singular.connect.local.LocalConnector;
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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class Scrubber implements BeanFactoryAware {

    @Value("${scrub}")
    private boolean scrub;

    @Autowired
    private Locations locations;

    private BeanFactory beanFactory;

    private Logger LOGGER = LoggerFactory.getLogger(Scrubber.class);

    private final static Map<String, String> APPS = new HashMap();

    @PostConstruct
    public void start() {
        if(scrub) {
            fillApplicationLocations();
            LOGGER.info("Starting Scrubber.");
            for (final Map.Entry<String, String> appLocation : APPS.entrySet()) {

                Slicer slicer = createSlicer(appLocation.getKey(), true);
                TailerListener tailerListener = new FileTailer(slicer);
                LOGGER.info("Starting Tailer on " + appLocation.getKey() + " logs...");
                Tailer.create(new File(appLocation.getValue()), tailerListener, 50L, true);

                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            LOGGER.info("Reading logs from " + appLocation.getKey() + " from back to front...");
                            retrieveLocalLogs(appLocation.getKey(), appLocation.getValue());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }
        }
    }

    private void retrieveLocalLogs(String name, String path) throws IOException {
        LocalConnector connector = new LocalConnector();
        BufferedReader reader = connector.readFileBackwards(path);
        ReverseSlicer slicer = createReverseSlicer(name, false);
        String line;

        LOGGER.info("Starting to read from " + name);
        while ((line = reader.readLine()) != null) {
            sliceLogs(slicer, line);
        }

        LOGGER.info("Connector for " + name + " found no more logs, disconnecting...");
        reader.close();
        LOGGER.info("Connector for " + name + " disconnected.");
    }

    private Slicer createSlicer(String name, boolean tailer) {
        Slicer slicer = (Slicer) beanFactory.getBean("slicer");
        slicer.setHost(name);
        slicer.setTailer(tailer);
        return slicer;
    }

    private ReverseSlicer createReverseSlicer(String name, boolean tailer) {
        ReverseSlicer slicer = (ReverseSlicer) beanFactory.getBean("reverseSlicer");
        slicer.setHost(name);
        slicer.setTailer(tailer);
        return slicer;
    }

    private void sliceLogs(ReverseSlicer slicer, String line) throws IOException {
        LOGGER.debug(line);
        slicer.process(line);
    }

    private void fillApplicationLocations() {
        for (String site : locations.getLocations()) {
            String[] components = site.split(":");
            LOGGER.info(components.toString());
            String host = components[0];
            String path = components[1];
            APPS.put(host, path);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}