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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class DataController {

    @Value("${timeslice}")
    private int timeslice;

    @Value("${server.port}")
    private int port;

    @Autowired
    private FileManager fileManager;

    @Autowired
    private TableChartCreator tableChartCreator;

    private Logger LOGGER = LoggerFactory.getLogger(DataController.class);

    @RequestMapping("tablechart")
    public ResponseEntity tablechart(@RequestParam(value="host") String host, @RequestParam(value="from") String from, @RequestParam(value="till") String till, @RequestParam(value="slice") int slice)throws IOException, InterruptedException {
        if(slice < timeslice) {
            LOGGER.error(String.format("Tablechart request: http://localhost:%d/tablechart?host=%s&from=%s&till=%s&slice=%d Slice requested is smaller than actual timeslices.", port, host, from, till, slice));
            return ResponseEntity.badRequest().body(String.format("Requested slice cannot be smaller than stored slices.  Currently slices are stored per %d minutes.", timeslice));
        }
        LOGGER.info(String.format("Tablechart request: http://localhost:%d/tablechart?host=%s&from=%s&till=%s&slice=%d", port, host, from, till, slice));
        String formattedTime = from.replace(" ", "T");
        DateTime fromTime = new DateTime(formattedTime);
        formattedTime = till.replace(" ", "T");
        DateTime tillTime = new DateTime(formattedTime);
        return ResponseEntity.ok(tableChartCreator.create(host, fromTime, tillTime, slice));
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
