package org.singular.entities;

public class BarchartData {

    private String tag;
    private Double average;

    public BarchartData() {
    }

    public BarchartData(String tag, Double average) {
        this.tag = tag;
        this.average = average;
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

    @Override
    public String toString() {
        return "[" + tag + ", " + average + "]";
    }
}
