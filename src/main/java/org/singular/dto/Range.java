package org.singular.dto;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Range)) return false;

        Range range = (Range) o;

        if (start != null ? !start.equals(range.start) : range.start != null) return false;
        return end != null ? end.equals(range.end) : range.end == null;

    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return fmt.print(start) + " to " + fmt.print(end);
    }
}
