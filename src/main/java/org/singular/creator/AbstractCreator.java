package org.singular.creator;

import com.google.common.collect.Lists;
import javafx.util.Pair;
import org.joda.time.DateTime;
import org.singular.dto.Dataset;
import org.singular.dto.LogLine;
import org.singular.files.FileManager;
import org.singular.files.FileReader;
import org.singular.parser.LogParser;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCreator<S extends Dataset> {

    @Autowired
    private FileManager fileManager;

    @Autowired
    private FileReader fileReader;

    @Autowired
    private LogParser logParser;

    protected String header;
    protected int slice;
    protected Map<String, List<Pair<DateTime, Long>>> metricsPerTag = new HashMap<String, List<Pair<DateTime, Long>>>();

    public S create(String host, String header, String from, int slice) throws IOException {
        this.header = header;
        this.slice = slice;
        String logs = fileReader.getContent(fileManager.getFilteredFilesWithinRange(host, from, slice));
        List<LogLine> logLines = logParser.parseLogs(logs);

        for(LogLine logLine : logLines) {
            List pairs = metricsPerTag.get(logLine.getTag());
            Pair<DateTime, Long> pair = new Pair<DateTime, Long>(logLine.getStart(), logLine.getDuration());

            if(pairs == null) {
                metricsPerTag.put(logLine.getTag(), Lists.newArrayList(pair));
            } else {
                pairs.add(pair);
            }
        }

        return calculate();
    }

    public S create(String host, String header, String from) throws IOException {
        return create(host, header, from, 0);
    }

    protected abstract S calculate();
}
