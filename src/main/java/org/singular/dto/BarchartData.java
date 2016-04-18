package org.singular.dto;

public class BarchartData extends Data {

    private String tag;
    private Double average;
    private long count;

    public BarchartData() {
    }

    public BarchartData(String tag, Double average, long count) {
        this.tag = tag;
        this.average = average;
        this.count = count;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "BarchartData{" +
                "tag='" + tag + '\'' +
                ", average=" + average +
                ", count=" + count +
                '}';
    }
}
