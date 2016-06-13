package org.singular.dto;

public class TagData extends Data {

    private String tag;
    private long min;
    private long max;
    private Double average;
    private Double stdDev;
    private long count;

    public TagData() {
    }

    public TagData(String tag, Double average, long count) {
        this.tag = tag;
        this.average = average;
        this.count = count;
    }

    public TagData(String tag, long min, long max, Double average, Double stdDev, long count) {
        this.tag = tag;
        this.min = min;
        this.max = max;
        this.average = average;
        this.stdDev = stdDev;
        this.count = count;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getStdDev() {
        return stdDev;
    }

    public void setStdDev(Double stdDev) {
        this.stdDev = stdDev;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "TagData{" +
                "tag='" + tag + '\'' +
                ", min=" + min +
                ", max=" + max +
                ", average=" + average +
                ", stdDev=" + stdDev +
                ", count=" + count +
                '}';
    }
}
