package org.singular.controllers;

import org.singular.entities.BarchartData;
import org.singular.entities.Perf4jLog;
import org.singular.entities.Perf4jLogLine;

import java.util.ArrayList;
import java.util.List;

public class Perf4jDatasetTransformer {

    public List<BarchartData> transform(Perf4jLog logs) {
        List<BarchartData> dataList = new ArrayList<BarchartData>();
        for(Perf4jLogLine logLine : logs.getLogLines()) {
            dataList.add(new BarchartData(logLine.getTag(), logLine.getAverage()));
        }
        return dataList;
    }
}
