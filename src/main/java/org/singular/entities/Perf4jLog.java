package org.singular.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.singular.util.RangeDeserializer;
import org.singular.util.RangeSerializer;

import java.util.LinkedList;
import java.util.List;

public class Perf4jLog implements Comparable<Perf4jLog> {
    @JsonSerialize(using = RangeSerializer.class)
    @JsonDeserialize(using = RangeDeserializer.class)
    private Range range;
    private List<Perf4jLogLine> logLines = new LinkedList<Perf4jLogLine>();

    public Perf4jLog() {
    }

    public Perf4jLog(Range range, List<Perf4jLogLine> logLines) {
        this.range = range;
        this.logLines = logLines;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public List<Perf4jLogLine> getLogLines() {
        return logLines;
    }

    public void setLogLines(List<Perf4jLogLine> logLines) {
        this.logLines = logLines;
    }

    public void addLogLine(Perf4jLogLine logLine) {
        logLines.add(logLine);
    }

    @Override
    public int compareTo(Perf4jLog o) {
        return this.range.compareTo(o.range);
    }
}
