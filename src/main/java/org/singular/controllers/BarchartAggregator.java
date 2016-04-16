package org.singular.controllers;

import org.joda.time.DateTime;
import org.singular.fileManager.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class BarchartAggregator {

    @Autowired
    private FileManager fileManager;

    private final String rootDir = System.getProperty("user.dir") + "/logs/";

    public List<File> aggregate(String host, DateTime start, int slice) {
        List<File> applicableLogFiles = new ArrayList<File>();
        List<String> fileNames = fileManager.getFilteredFiles(host);

        DateTime startTime;
        DateTime endTime;
        for(String fileName : fileNames) {
            startTime = new DateTime(fileManager.getStartFormatted(fileName));
            endTime = new DateTime(fileManager.getEndFormatted(fileName));
            if(startTime.isEqual(start) || startTime.isAfter(start)) {
                if(endTime.isBefore(start.plusMinutes(slice)) || endTime.isEqual(start.plusMinutes(slice))) {
                    applicableLogFiles.add(new File(rootDir + fileName));
                }
            }
        }
        return applicableLogFiles;
    }
}
