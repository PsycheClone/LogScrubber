package org.singular.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.singular.entities.Log;
import org.singular.parser.DatasetConverter;
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
    private ObjectMapper objectMapper;

    String rootDir = System.getProperty("user.dir") + "/src/test/resources/";
    String logsDir = System.getProperty("user.dir") + "/logs/json/";

    @CrossOrigin(origins = "http://localhost:8090")
    @RequestMapping("json")
    public String test() throws IOException {
        File jsonFile = new File(rootDir + "jsonLog.log");
        return getContent(jsonFile);
    }

    private String getContent(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String content = "";
        String line;
        while ((line = reader.readLine()) != null) {
            content = content + line;
        }
        Log log = objectMapper.readValue(content, Log.class);
        return datasetConverter.convertToBarchart(log);
    }
}
