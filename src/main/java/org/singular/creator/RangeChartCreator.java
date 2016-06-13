package org.singular.creator;

import com.google.common.collect.Lists;
import javafx.util.Pair;
import org.joda.time.DateTime;
import org.singular.dto.*;
import org.singular.dto.util.TagDataAverageAscendingComparator;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class RangeChartCreator extends AbstractCreator<RangeDataset> {

    @Override
    protected Data createDataset(Map.Entry<String, List<Pair<DateTime, Long>>> metrics) {
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

    @Override
    protected List<File> getLogs(String host, String from, int range) throws IOException {
        return fileManager.getFilteredFilesWithinRange(host, from, range);
    }

    @Override
    protected List<RangeDataset> calculate(List<File> filteredFiles) throws IOException {
//        List<LogLine> logLines = logParser.parseLogs(fileReader.getContent(filteredFiles));
//        calculateMetrics(logLines);
//
//        RangeDataset rangeDataset = new RangeDataset();
//        rangeDataset.setRange(new Range(new DateTime(datasetHeader), new DateTime(datasetHeader).plusMinutes(range)));
//
//        averagePerSlice(rangeDataset);
//
//        Collections.sort(rangeDataset.getDataset(), new TagDataAverageAscendingComparator());
//        return Lists.newArrayList(rangeDataset);
        return null;
    }
}
