package org.singular.controllers;

import org.singular.entities.BarchartLog;
import org.singular.fileManager.FileManager;
import org.singular.parser.MelexisLogParser;
import org.singular.parser.Perf4jLogParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class DataController {

    @Autowired
    private FileManager fileManager;

    @Autowired
    private MelexisLogParser melexisLogParser;

    @Autowired
    private Perf4jLogParser perf4jLogParser;

    private Perf4jDatasetTransformer perf4jDatasetTransformer = new Perf4jDatasetTransformer();

    private Logger LOGGER = LoggerFactory.getLogger(DataController.class);

    String rootDir = System.getProperty("user.dir") + "/src/test/resources/";
    String logsDir = System.getProperty("user.dir") + "/logs/";

    @CrossOrigin(origins = "http://localhost:8081")
    @RequestMapping("json")
    public BarchartLog perf4jBarchart(@RequestParam(value="host") String host, @RequestParam(value="from") long from, @RequestParam(value="till") long till)throws IOException, InterruptedException {
        LOGGER.info("Barchart request for " + host + " from: " + from + " till: " + till);
        File jsonFile = new File(logsDir + createFileName(host, from, till));
        return prepareBarchartData(jsonFile);
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @RequestMapping("timeslices")
    public List<String> timeslices(@RequestParam(value="host") String host) {
        return fileManager.getAvailableRanges(host);
    }

    private String createFileName(String host, long from, long till) {
        return host + "_" + from + "_" + till + ".log";
    }

    private BarchartLog prepareBarchartData(File file) throws IOException, InterruptedException {
        return perf4jDatasetTransformer.transform(perf4jLogParser.processLogs(melexisLogParser.extractPerf4jLogs(file)));
    }
}
