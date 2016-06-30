package org.singular.creator;

import com.google.common.collect.Lists;
import javafx.util.Pair;
import org.joda.time.DateTime;
import org.singular.dto.*;
import org.singular.files.FileGrouper;
import org.singular.files.FileManager;
import org.singular.files.FileReader;
import org.singular.parser.LogParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class TableChartCreator {

    @Autowired
    protected FileManager fileManager;

    private long min;
    private long max;
    private Map<String, List<Pair<DateTime, Long>>> metricsPerTag = new HashMap<String, List<Pair<DateTime, Long>>>();
    private Map<String, Long> maxValues = new HashMap<String, Long>();
    private Map<String, Long> minValues = new HashMap<String, Long>();

    public TableRangeDataset create(String host, DateTime from, DateTime till, int slice) throws IOException {
        List<File> files = fileManager.getFilteredFilesWithinRange(host, from, till);
        List<List<File>> groupedFiles = FileGrouper.groupBySlice(files, slice);
        return calculate(groupedFiles);
    }

    private TableRangeDataset calculate(List<List<File>> filesList) throws IOException {
        TableRangeDataset tableRangeDataset = new TableRangeDataset();
        List<RangeDataset> rangeDatasets = new LinkedList<RangeDataset>();
        for(List<File> fileList : filesList) {
            DateTime fromTime = new DateTime(fileManager.getStartParsable(fileList.get(0).getName()));
            DateTime tillTime = new DateTime(fileManager.getEndParsable(fileList.get(fileList.size()-1).getName()));

            String logs = FileReader.getContent(fileList);
            List<LogLine> logLines = LogParser.parseLogs(logs);
            calculateMetrics(logLines);

            RangeDataset rangeDataset = new RangeDataset();
            rangeDataset.setRange(new Range(fromTime, tillTime));
            createAndAdd(rangeDataset);
            rangeDatasets.add(rangeDataset);
        }
        tableRangeDataset.setRangeDatasets(rangeDatasets);
        return tableRangeDataset;
    }

    private void calculateMetrics(List<LogLine> logLines) {
        metricsPerTag.clear();
        for (LogLine logLine : logLines) {
            String tag = logLine.getTag();
            List pairs = metricsPerTag.get(logLine.getTag());
            Pair<DateTime, Long> pair = new Pair<DateTime, Long>(logLine.getStart(), logLine.getDuration());

            if(maxValues.get(tag) != null) {
                max = maxValues.get(tag);
                if(logLine.getDuration() > max) {
                    maxValues.put(tag, logLine.getDuration());
                }
            } else {
                maxValues.put(tag, logLine.getDuration());
            }

            if(minValues.get(tag) != null) {
                min = minValues.get(tag);
                if(logLine.getDuration() < min) {
                    minValues.put(tag, logLine.getDuration());
                }
            } else {
                minValues.put(tag, logLine.getDuration());
            }

            if (pairs == null) {
                metricsPerTag.put(logLine.getTag(), Lists.newArrayList(pair));
            } else {
                pairs.add(pair);
            }
        }
    }

    private void createAndAdd(RangeDataset dataset) {
        for(Map.Entry<String, List<Pair<DateTime, Long>>> metrics : metricsPerTag.entrySet()) {
            if(metrics.getKey() != null && metrics.getValue() != null) {
                Data data = createDataset(metrics);
                dataset.addToDataset(data);
            }
        }
    }

    private Data createDataset(Map.Entry<String, List<Pair<DateTime, Long>>> metrics) {
        double average = 0;
        for (Pair<DateTime, Long> pair : metrics.getValue()) {
            average = average + pair.getValue();
        }
        average = average / metrics.getValue().size();
        TagData averageTagData = new TagData();
        double stdDev = calculateStdDev(average, metrics.getValue());

        averageTagData.setTag(metrics.getKey());
        averageTagData.setMin(minValues.get(metrics.getKey()));
        averageTagData.setMax(maxValues.get(metrics.getKey()));
        averageTagData.setAverage(new BigDecimal(average).setScale(1, RoundingMode.HALF_UP).doubleValue());
        averageTagData.setStdDev(new BigDecimal(stdDev).setScale(1, RoundingMode.HALF_UP).doubleValue());
        averageTagData.setCount(metrics.getValue().size());

        return averageTagData;
    }

    private double calculateStdDev(double average, List<Pair<DateTime, Long>> metrics) {
        double stdDev = 0;
        for(Pair<DateTime, Long> metric : metrics) {
            stdDev += Math.pow((metric.getValue() - average), 2);
        }
        return Math.sqrt(stdDev / metrics.size());
    }
}
