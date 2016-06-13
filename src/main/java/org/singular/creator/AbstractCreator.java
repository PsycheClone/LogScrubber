package org.singular.creator;

import com.google.common.collect.Lists;
import javafx.util.Pair;
import org.joda.time.DateTime;
import org.singular.dto.Data;
import org.singular.dto.Dataset;
import org.singular.dto.LogLine;
import org.singular.dto.TagData;
import org.singular.files.FileManager;
import org.singular.files.FileReader;
import org.singular.parser.LogParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCreator<S extends Dataset> {

    @Value("${timeslice}")
    private int timeslice;

    @Autowired
    protected FileManager fileManager;

    @Autowired
    protected FileReader fileReader;

    @Autowired
    protected LogParser logParser;

    private long min;
    private long max;
    private Map<String, List<Pair<DateTime, Long>>> metricsPerTag = new HashMap<String, List<Pair<DateTime, Long>>>();
    protected Map<String, Long> maxValues = new HashMap<String, Long>();
    protected Map<String, Long> minValues = new HashMap<String, Long>();
    protected String datasetHeader;
    protected int range;
    protected int slice;

    public List<S> create(String host, String from, int range) throws IOException {
        this.range = range;
        this.slice = timeslice;
        List<File> filteredFiles = getLogs(host, from, range);

        return calculate(filteredFiles);
    }

    public List<S> create(String host, String from, int range, int slice) throws IOException {
        this.range = range;
        this.slice = slice;
        List<File> filteredFiles = getLogs(host, from, range);

        return calculate(filteredFiles);
    }

    public List<S> create(String host, String header, String from, int range) throws IOException {
        this.datasetHeader = header;
        return create(host, from, range);
    }

    protected void calculateMetrics(List<LogLine> logLines) {
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

    protected void averagePerSlice(S dataset) {
        for(Map.Entry<String, List<Pair<DateTime, Long>>> metrics : metricsPerTag.entrySet()) {
            if(metrics.getKey() != null && metrics.getValue() != null) {
                Data data = createDataset(metrics);
                dataset.addToDataset(data);
            }
        }
    }

    protected double calculateStdDev(double average, List<Pair<DateTime, Long>> metrics) {
        double stdDev = 0;
        for(Pair<DateTime, Long> metric : metrics) {
            stdDev += Math.pow((metric.getValue() - average), 2);
        }
        return Math.sqrt(stdDev / metrics.size());
    }

    protected abstract Data createDataset(Map.Entry<String, List<Pair<DateTime, Long>>> metrics);

    protected abstract List<File> getLogs(String host, String from, int range) throws IOException;

    protected abstract List<S> calculate(List<File> files) throws IOException;
}
