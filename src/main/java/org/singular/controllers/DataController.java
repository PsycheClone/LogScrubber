package org.singular.controllers;

import org.joda.time.DateTime;
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

    @Autowired
    private BarchartAggregator barchartAggregator;

    private BarchartDatasetTransformer perf4jDatasetTransformer = new BarchartDatasetTransformer();

    private Logger LOGGER = LoggerFactory.getLogger(DataController.class);

    String rootDir = System.getProperty("user.dir") + "/src/test/resources/";
    String logsDir = System.getProperty("user.dir") + "/logs/";

    @CrossOrigin(origins = "http://localhost:8081")
    @RequestMapping("json")
    public BarchartLog perf4jBarchart(@RequestParam(value="host") String host, @RequestParam(value="from") String from, @RequestParam(value="slice") int slice)throws IOException, InterruptedException {
        LOGGER.info("Barchart request for " + host + " from: " + from + " slice: " + slice);
        if(slice <= 5) {
            File jsonFile = new File(logsDir + createFileName(host, from, slice));
            return prepareBarchartData(jsonFile, Integer.valueOf(slice));
        }
        return prepareBarchartData(barchartAggregator.aggregate(host, new DateTime(from.replace(" ", "T")), Integer.valueOf(slice)), Integer.valueOf(slice));
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @RequestMapping("timeslices")
    public List<String> timeslices(@RequestParam(value="host") String host) {
        return fileManager.getAvailableStartTimes(host);
    }

    private String createFileName(String host, String from, int slice) {
        from = from.replace(" ", "T");
        DateTime fromTime = new DateTime(from);
        return host + "_" + fromTime.getMillis() + "_" + fromTime.plusMinutes(slice-(slice%5)).getMillis() + ".log";
    }

    private BarchartLog prepareBarchartData(File file, int slice) throws IOException, InterruptedException {
        return perf4jDatasetTransformer.transform(perf4jLogParser.processLogs(melexisLogParser.extractPerf4jLogs(file, slice)));
    }

    private BarchartLog prepareBarchartData(List<File> files, int slice) throws IOException, InterruptedException {
        return perf4jDatasetTransformer.transform(perf4jLogParser.processLogs(melexisLogParser.extractPerf4jLogs(files, slice)));
    }
}
