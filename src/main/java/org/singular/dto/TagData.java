package org.singular.dto;

import java.util.Comparator;

public class TagData extends Data implements Comparator<TagData> {

    private String tag;
    private Double average;
    private long count;

    public TagData() {
    }

    public TagData(String tag, Double average, long count) {
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
    public int compare(TagData d1, TagData d2) {
        return d1.getAverage() == d2.getAverage() ? 0 : d1.getAverage() < d2.getAverage() ? -1 : 1;
    }

    @Override
    public String toString() {
        return "TagData{" +
                "tag='" + tag + '\'' +
                ", average=" + average +
                ", count=" + count +
                '}';
    }
}
