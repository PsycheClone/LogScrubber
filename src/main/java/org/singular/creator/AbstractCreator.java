package org.singular.creator;

import com.google.common.collect.Lists;
import javafx.util.Pair;
import org.joda.time.DateTime;
import org.singular.dto.Dataset;
import org.singular.dto.LogLine;
import org.singular.dto.TagData;
import org.singular.files.FileManager;
import org.singular.files.FileReader;
import org.singular.parser.LogParser;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCreator<S extends Dataset> {

    @Autowired
    protected FileManager fileManager;

    @Autowired
    protected FileReader fileReader;

    @Autowired
    protected LogParser logParser;

    protected Map<String, List<Pair<DateTime, Long>>> metricsPerTag = new HashMap<String, List<Pair<DateTime, Long>>>();
    protected String datasetHeader;
    protected int range;

    public List<S> create(String host, String from, int range) throws IOException {
        this.range = range;
        List<File> filteredFiles = getLogs(host, from, range);

        return calculate(filteredFiles);
    }

    public List<S> create(String host, String header, String from, int range) throws IOException {
        this.datasetHeader = header;
        return create(host, from, range);
    }

    protected void aggregateMetrics(List<LogLine> logLines) {
        metricsPerTag.clear();
        for (LogLine logLine : logLines) {
            List pairs = metricsPerTag.get(logLine.getTag());
            Pair<DateTime, Long> pair = new Pair<DateTime, Long>(logLine.getStart(), logLine.getDuration());

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
                TagData averageTagData = new TagData();
                double average = 0;
                for (Pair<DateTime, Long> pair : metrics.getValue()) {
                    average = average + pair.getValue();
                }
                average = average / metrics.getValue().size();

                averageTagData.setTag(metrics.getKey());
                averageTagData.setAverage(new BigDecimal(average).setScale(0, RoundingMode.HALF_UP).doubleValue());
                averageTagData.setCount(metrics.getValue().size());

                dataset.addToDataset(averageTagData);
            }
        }
    }

    protected abstract List<File> getLogs(String host, String from, int range) throws IOException;

    protected abstract List<S> calculate(List<File> files) throws IOException;
}
