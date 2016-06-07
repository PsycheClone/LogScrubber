package org.singular.controllers;

import com.google.common.collect.Lists;
import org.singular.creator.RangeChartCreator;
import org.singular.creator.TableChartCreator;
import org.singular.dto.RangeDataset;
import org.singular.dto.TableRangeDataset;
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

    @Autowired
    private TableChartCreator tableChartCreator;

    private Logger LOGGER = LoggerFactory.getLogger(DataController.class);

    @RequestMapping("json")
    public RangeDataset perf4jBarchart(@RequestParam(value="host") String host, @RequestParam(value="from") String from, @RequestParam(value="slice") int slice)throws IOException, InterruptedException {
        LOGGER.info("Barchart request for " + host + " from: " + from + " range: " + slice);
        String formattedTime = from.replace(" ", "T");
        return rangeChartCreator.create(host, formattedTime, formattedTime, slice).get(0);
    }

    @RequestMapping("tablechart")
    public TableRangeDataset tablechart(@RequestParam(value="host") String host, @RequestParam(value="from") String from, @RequestParam(value="range") int range)throws IOException, InterruptedException {
        LOGGER.info("Tablechart request for " + host + " from: " + from);
        String formattedTime = from.replace(" ", "T");
        TableRangeDataset tableRangeDataset = new TableRangeDataset();
        tableRangeDataset.setRangeDatasets(tableChartCreator.create(host, formattedTime, range));
        return tableRangeDataset;
    }

    @RequestMapping("environments")
    public List<String> environments() {
        return Lists.newArrayList(fileManager.getAllHosts());
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
