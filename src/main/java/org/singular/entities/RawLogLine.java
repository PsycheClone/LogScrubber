package org.singular.entities;

public class RawLogLine {

    private String host;
    private LogLine logline;

    public RawLogLine() {
    }

    public RawLogLine(String host, LogLine logline) {
        this.host = host;
        this.logline = logline;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public LogLine getLogline() {
        return logline;
    }

    public void setLogline(LogLine logline) {
        this.logline = logline;
    }
}
