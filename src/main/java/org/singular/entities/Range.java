package org.singular.entities;

import org.joda.time.DateTime;

/**
 * Created by Sven on 25/03/2016.
 */
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
}
