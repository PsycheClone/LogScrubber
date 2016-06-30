package org.singular.controllers;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.singular.creator.TableChartCreator;
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
    private TableChartCreator tableChartCreator;

    private Logger LOGGER = LoggerFactory.getLogger(DataController.class);

    @RequestMapping("tablechart")
    public TableRangeDataset tablechart(@RequestParam(value="host") String host, @RequestParam(value="from") String from, @RequestParam(value="till") String till, @RequestParam(value="slice") int slice)throws IOException, InterruptedException {
        LOGGER.info("Tablechart request for " + host + " from: " + from + " till: " + till);
        String formattedTime = from.replace(" ", "T");
        DateTime fromTime = new DateTime(formattedTime);
        formattedTime = till.replace(" ", "T");
        DateTime tillTime = new DateTime(formattedTime);
        return tableChartCreator.create(host, fromTime, tillTime, slice);
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
