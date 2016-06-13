package org.singular.creator;

import javafx.util.Pair;
import org.joda.time.DateTime;
import org.singular.dto.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class TableChartCreator extends AbstractCreator<RangeDataset> {

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
    protected List<RangeDataset> calculate(List<File> files) throws IOException {
        List<RangeDataset> rangeDatasets = new LinkedList<RangeDataset>();
        for(File file : files) {
            DateTime fromTime = new DateTime(fileManager.getStartParsable(file.getName()));
            DateTime tillTime = new DateTime(fileManager.getEndParsable(file.getName()));
            String logs = fileReader.getContent(file);
            List<LogLine> logLines = logParser.parseLogs(logs);
            calculateMetrics(logLines);

            RangeDataset rangeDataset = new RangeDataset();
            rangeDataset.setRange(new Range(fromTime, tillTime));
            averagePerSlice(rangeDataset);
            rangeDatasets.add(rangeDataset);
        }
        return rangeDatasets;
    }
}
