package org.singular.creator;

import org.joda.time.DateTime;
import org.singular.calculate.Calculator;
import org.singular.dto.LogLine;
import org.singular.dto.Range;
import org.singular.dto.RangeDataset;
import org.singular.dto.TableRangeDataset;
import org.singular.files.FileGrouper;
import org.singular.files.FileManager;
import org.singular.files.FileReader;
import org.singular.parser.LogParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Component
public class TableChartCreator {

    @Autowired
    private FileManager fileManager;

    private Calculator calculator = new Calculator();

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

            RangeDataset rangeDataset = new RangeDataset();
            rangeDataset.setRange(new Range(fromTime, tillTime));
            calculator.calculateAndAdd(logLines, rangeDataset);
            rangeDatasets.add(rangeDataset);
        }
        tableRangeDataset.setRangeDatasets(rangeDatasets);
        return tableRangeDataset;
    }
}
