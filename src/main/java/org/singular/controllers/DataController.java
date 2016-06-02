package org.singular.controllers;

import org.singular.creator.RangeChartCreator;
import org.singular.dto.RangeDataset;
import org.singular.files.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class DataController {

    @Value("${timeslice}")
    private int timeslice;

    @Autowired
    private FileManager fileManager;

    @Autowired
    private RangeChartCreator rangeChartCreator;

    private Logger LOGGER = LoggerFactory.getLogger(DataController.class);

    @RequestMapping("json")
    public RangeDataset perf4jBarchart(@RequestParam(value="host") String host, @RequestParam(value="from") String from, @RequestParam(value="slice") int slice)throws IOException, InterruptedException {
        LOGGER.info("Barchart request for " + host + " from: " + from + " slice: " + slice);
        String formattedTime = from.replace(" ", "T");
        return rangeChartCreator.create(host, formattedTime, formattedTime, slice);
    }

    @RequestMapping("tablechart")
    public RangeDataset tablechart(@RequestParam(value="host") String host, @RequestParam(value="from") String from, @RequestParam(value="slice") int slice)throws IOException, InterruptedException {
        LOGGER.info("Barchart request for " + host + " from: " + from + " slice: " + slice);
        String formattedTime = from.replace(" ", "T");
        return rangeChartCreator.create(host, formattedTime, formattedTime, slice);
    }

    @RequestMapping("ranges")
    public List<String> ranges(@RequestParam(value="host") String host) {
        return fileManager.getAvailableStartTimes(host);
    }

    @RequestMapping("timeslice")
    public int timeslice() {
        return timeslice;
    }
}
