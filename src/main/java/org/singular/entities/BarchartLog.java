package org.singular.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.singular.util.RangeDeserializer;
import org.singular.util.RangeSerializer;

import java.util.LinkedList;
import java.util.List;

public class BarchartLog implements Comparable<BarchartLog> {
    @JsonSerialize(using = RangeSerializer.class)
    @JsonDeserialize(using = RangeDeserializer.class)
    private Range range;
    private List<BarchartData> logLines = new LinkedList<BarchartData>();

    public BarchartLog() {
    }

    public BarchartLog(Range range, List<BarchartData> logLines) {
        this.range = range;
        this.logLines = logLines;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public List<BarchartData> getLogLines() {
        return logLines;
    }

    public void setLogLines(List<BarchartData> logLines) {
        this.logLines = logLines;
    }

    public void addLogLine(BarchartData logLine) {
        logLines.add(logLine);
    }

    @Override
    public int compareTo(BarchartLog o) {
        return this.range.compareTo(o.range);
    }
}
