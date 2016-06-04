package org.singular.creator;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.singular.dto.LogLine;
import org.singular.dto.Range;
import org.singular.dto.RangeDataset;
import org.singular.dto.util.TagDataAverageAscendingComparator;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class RangeChartCreator extends AbstractCreator<RangeDataset> {



    @Override
    protected List<File> getLogs(String host, String from, int range) throws IOException {
        return fileManager.getFilteredFilesWithinRange(host, from, range);
    }

    @Override
    protected List<RangeDataset> calculate(List<File> filteredFiles) throws IOException {
        List<LogLine> logLines = logParser.parseLogs(fileReader.getContent(filteredFiles));
        aggregateMetrics(logLines);

        RangeDataset rangeDataset = new RangeDataset();
        rangeDataset.setRange(new Range(new DateTime(datasetHeader), new DateTime(datasetHeader).plusMinutes(range)));

        averagePerSlice(rangeDataset);

        rangeDataset.getDataset().sort(new TagDataAverageAscendingComparator());
        return Lists.newArrayList(rangeDataset);
    }
}
