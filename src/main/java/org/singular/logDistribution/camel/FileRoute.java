package org.singular.logDistribution.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.singular.fileManager.FileManager;
import org.singular.logDistribution.Slicer;
import org.singular.parser.MelexisLogParser;
import org.singular.parser.Perf4jLogParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;

@Component
public class FileRoute extends RouteBuilder{

    @Autowired
    private FileManager fileManager;

    @Autowired
    private Perf4jLogParser perf4jParser;

    @Autowired
    private Slicer slicer;

    @Autowired
    private MelexisLogParser melexisLogParser;

    @Value("${hosts}")
    private String[] hosts;

    private Logger LOGGER = LoggerFactory.getLogger(FileRoute.class);

    private final String rootDir = System.getProperty("user.dir") + "/logs/";
    private final String logsDir = rootDir + "downloaded/";
    private final String perfDir = rootDir + "json/";

    @Override
    public void configure() throws Exception {
        File logsFolder = fileManager.createFolderIfNotExists(logsDir);
        File perf4jFolder = fileManager.createFolderIfNotExists(perfDir);

        for(String host : Arrays.asList(hosts)) {
            from("jms:"+host)
                    .setHeader("id", simple(host))
                    .aggregate(header("id"), new LogAggregateStrategy()).completionSize(1000).completionTimeout(20000)
                    .bean(slicer, "process");
//                    .process(storeAggregatedLogLines)
//                    .to("log:this.is.awesome");
        }
        from("file:" + logsDir + "?idempotent=true")
                .threads(10)
                .bean(melexisLogParser, "extractPerf4jLogs");

//        from("file:" + perf4jFolder + "?idempotent=true")
//                .threads(10)
//                .bean(perf4jParser, "processLogs");
    }

    Processor storeAggregatedLogLines = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            fileManager.storeFile(logsDir, exchange.getIn().getHeader("id").toString(), exchange.getIn().getBody().toString());
        }
    };
}
