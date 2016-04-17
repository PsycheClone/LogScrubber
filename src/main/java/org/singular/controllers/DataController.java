package org.singular.controllers;

import org.singular.creator.BarchartCreator;
import org.singular.dto.BarchartDataset;
import org.singular.files.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class DataController {

    @Autowired
    private FileManager fileManager;

    @Autowired
    private BarchartCreator barchartCreator;

    private Logger LOGGER = LoggerFactory.getLogger(DataController.class);

    @CrossOrigin(origins = "http://localhost:8081")
    @RequestMapping("json")
    public BarchartDataset perf4jBarchart(@RequestParam(value="host") String host, @RequestParam(value="from") String from, @RequestParam(value="slice") int slice)throws IOException, InterruptedException {
        LOGGER.info("Barchart request for " + host + " from: " + from + " slice: " + slice);
        String formattedTime = from.replace(" ", "T");
        return barchartCreator.create(host, formattedTime, formattedTime, slice);
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @RequestMapping("timeslices")
    public List<String> timeslices(@RequestParam(value="host") String host) {
        return fileManager.getAvailableStartTimes(host);
    }
}
