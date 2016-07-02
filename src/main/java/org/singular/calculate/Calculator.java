package org.singular.calculate;

import com.google.common.collect.Lists;
import javafx.util.Pair;
import org.joda.time.DateTime;
import org.singular.dto.Data;
import org.singular.dto.LogLine;
import org.singular.dto.RangeDataset;
import org.singular.dto.TagData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculator {

    private long min;
    private long max;
    private Map<String, List<Pair<DateTime, Long>>> metricsPerTag = new HashMap<String, List<Pair<DateTime, Long>>>();
    private Map<String, Long> maxValues = new HashMap<String, Long>();
    private Map<String, Long> minValues = new HashMap<String, Long>();

    public void calculateAndAdd(List<LogLine> logLines, RangeDataset rangeDataset) {
        calculateMetrics(logLines);
        createAndAdd(rangeDataset);
    }

    private void calculateMetrics(List<LogLine> logLines) {
        min = Long.MIN_VALUE;
        max = Long.MAX_VALUE;
        metricsPerTag.clear();
        maxValues.clear();
        minValues.clear();
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
