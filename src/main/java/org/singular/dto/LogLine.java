package org.singular.dto;

import org.joda.time.DateTime;

public class LogLine {
    private DateTime start;
    private String tag;
    private Long duration;

    public LogLine() {
    }

    public LogLine(DateTime start, String tag, Long duration) {
        this.start = start;
        this.tag = tag;
        this.duration = duration;
    }

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "LogLine{" +
                "start=" + start +
                ", tag='" + tag + '\'' +
                ", duration=" + duration +
                '}';
    }
}
