package org.singular.entities;

public class LogLine {
    private String tag;
    private Double average;
    private Integer min;
    private Integer max;
    private Double StandardDeviation;
    private Integer Count;

    public LogLine() {}

    public LogLine(String tag, Double average, Integer min, Integer max, Double standardDeviation, Integer count) {
        this.tag = tag;
        this.average = average;
        this.min = min;
        this.max = max;
        StandardDeviation = standardDeviation;
        Count = count;
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

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Double getStandardDeviation() {
        return StandardDeviation;
    }

    public void setStandardDeviation(Double standardDeviation) {
        StandardDeviation = standardDeviation;
    }

    public Integer getCount() {
        return Count;
    }

    public void setCount(Integer count) {
        Count = count;
    }
}
