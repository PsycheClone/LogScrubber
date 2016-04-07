package org.singular.fileManager.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.singular.fileManager.FileManager;
import org.singular.parser.MelexisLogParser;
import org.singular.parser.Perf4jLogParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;

@Component
public class FileRoute{

    @Autowired
    private FileManager fileManager;

    @Autowired
    private Perf4jLogParser perf4jParser;

    @Autowired
    private MelexisLogParser melexisLogParser;

    private Logger LOGGER = LoggerFactory.getLogger(FileRoute.class);

    private final String rootDir = System.getProperty("user.dir") + "/logs/";
    private final String logsDir = rootDir + "downloaded/";
    private final String perfDir = rootDir + "perf4j/";

    final CamelContext camelContext = new DefaultCamelContext();

    @PostConstruct
    public void start() {
        final File logsFolder = fileManager.createFolderIfNotExists(logsDir);
        final File perf4jFolder = fileManager.createFolderIfNotExists(perfDir);
        try {
            camelContext.addRoutes(
                    new RouteBuilder() {
                        @Override
                        public void configure() throws Exception {
                            from("file:" + logsFolder + "?include=.*-done.*&idempotent=true")
                                    .threads(10)
                                    .bean(melexisLogParser, "extractPerf4jLogs");

                            from("file:" + perf4jFolder + "?idempotent=true")
                                    .threads(10)
                                    .bean(perf4jParser, "processLogs");
                        }
                    }
            );
            camelContext.start();
            LOGGER.info("Camel File route started...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy() throws Exception {
        camelContext.stop();
        LOGGER.info("Camel context stopped...");
    }
}
