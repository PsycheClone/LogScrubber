package org.singular.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.singular.entities.Perf4jLog;
import org.singular.parser.DatasetConverter;
import org.singular.parser.MelexisLogParser;
import org.singular.parser.Perf4jLogParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@RestController
public class DataController {

    private DatasetConverter datasetConverter = new DatasetConverter();

    @Autowired
    private MelexisLogParser melexisLogParser;

    @Autowired
    private Perf4jLogParser perf4jLogParser;

    private Perf4jDatasetTransformer perf4jDatasetTransformer = new Perf4jDatasetTransformer();

    @Autowired
    private ObjectMapper objectMapper;

    String rootDir = System.getProperty("user.dir") + "/src/test/resources/";
    String logsDir = System.getProperty("user.dir") + "/logs/json/";

    @CrossOrigin(origins = "http://localhost:8090")
    @RequestMapping("json")
    public String test() throws IOException, InterruptedException {
        File jsonFile = new File(rootDir + "esb-a-test.erfurt.elex.be_1460277000000_1460277277000.log");
        return prepareBarchartData(jsonFile);
    }

    private String prepareBarchartData(File file) throws IOException, InterruptedException {
        return perf4jDatasetTransformer.transform(perf4jLogParser.processLogs(melexisLogParser.extractPerf4jLogs(file))).toString();
    }

    private String getContent(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String content = "";
        String line;
        while ((line = reader.readLine()) != null) {
            content = content + line;
        }
        Perf4jLog log = objectMapper.readValue(content, Perf4jLog.class);
        return datasetConverter.convertToBarchart(log);
    }
}
