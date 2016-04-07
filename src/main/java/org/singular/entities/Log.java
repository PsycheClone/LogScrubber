package org.singular.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.singular.util.RangeDeserializer;
import org.singular.util.RangeSerializer;

import java.util.LinkedList;
import java.util.List;

public class Log implements Comparable<Log> {
    @JsonSerialize(using = RangeSerializer.class)
    @JsonDeserialize(using = RangeDeserializer.class)
    private Range range;
    private List<LogLine> logLines = new LinkedList<LogLine>();

    public Log() {
    }

    public Log(Range range, List<LogLine> logLines) {
        this.range = range;
        this.logLines = logLines;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public List<LogLine> getLogLines() {
        return logLines;
    }

    public void setLogLines(List<LogLine> logLines) {
        this.logLines = logLines;
    }

    public void addLogLine(LogLine logLine) {
        logLines.add(logLine);
    }

    @Override
    public int compareTo(Log o) {
        return this.range.compareTo(o.range);
    }
}
