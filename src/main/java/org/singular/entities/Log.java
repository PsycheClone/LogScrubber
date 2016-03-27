package org.singular.entities;

import java.util.*;

public class Log {
    private Map<Range, List<LogLine>> logLines = new TreeMap<Range, List<LogLine>>();

    public Log() {
    }

    public Log(Map<Range, List<LogLine>> logLines) {
        this.logLines = logLines;
    }

    public Map<Range, List<LogLine>> getLogLines() {
        return logLines;
    }

    public void setLogLines(Map<Range, List<LogLine>> logLines) {
        this.logLines = logLines;
    }

    public void addLogLine(Range range, LogLine logLine) {
        if(logLines.get(range) == null) {
            logLines.put(range, new LinkedList<LogLine>());
        }
        logLines.get(range).add(logLine);
    }
}
