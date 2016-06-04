package org.singular.creator.table;

import org.joda.time.DateTime;
import org.singular.creator.AbstractCreator;
import org.singular.dto.LogLine;
import org.singular.dto.Range;
import org.singular.dto.RangeDataset;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Component
public class TableChartCreator extends AbstractCreator<RangeDataset> {

    @Override
    protected List<File> getLogs(String host, String from, int range) throws IOException {
        return fileManager.getFilteredFilesBeforeAndAfter(host, from, range);
    }

    @Override
    protected List<RangeDataset> calculate(List<File> files) throws IOException {
        List<RangeDataset> rangeDatasets = new LinkedList<RangeDataset>();
        for(File file : files) {
            DateTime fromTime = new DateTime(fileManager.getStartParsable(file.getName()));
            DateTime tillTime = new DateTime(fileManager.getEndParsable(file.getName()));
            String logs = fileReader.getContent(file);
            List<LogLine> logLines = logParser.parseLogs(logs);
            aggregateMetrics(logLines);

            RangeDataset rangeDataset = new RangeDataset();
            rangeDataset.setRange(new Range(fromTime, tillTime));
            averagePerSlice(rangeDataset);
            rangeDatasets.add(rangeDataset);
        }
        return rangeDatasets;
    }
}
