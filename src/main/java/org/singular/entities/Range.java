package org.singular.entities;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Range implements Comparable<Range> {
    private DateTime start;
    private DateTime end;

    public Range() {
    }

    public Range(DateTime start, DateTime end) {
        this.start = start;
        this.end = end;
    }

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public DateTime getEnd() {
        return end;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }

    public int compareTo(Range o) {
        if(this.start.isBefore(o.start)) return -1;
        if(this.start.isAfter(o.start)) return 1;
        return 0;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return fmt.print(start) + " to " + fmt.print(end);
    }
}
