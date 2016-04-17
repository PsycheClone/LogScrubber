package org.singular.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.singular.dto.util.RangeDeserializer;
import org.singular.dto.util.RangeSerializer;

import java.util.List;

public class BarchartDataset extends Dataset implements Comparable<BarchartDataset> {
    @JsonSerialize(using = RangeSerializer.class)
    @JsonDeserialize(using = RangeDeserializer.class)
    private Range range;

    public BarchartDataset() {
    }

    public BarchartDataset(Range range, List<BarchartData> logLines) {
        this.range = range;
        this.dataset = logLines;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    @Override
    public int compareTo(BarchartDataset o) {
        return this.range.compareTo(o.range);
    }
}
