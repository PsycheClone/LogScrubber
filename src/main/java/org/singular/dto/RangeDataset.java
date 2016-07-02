package org.singular.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.singular.dto.util.RangeDeserializer;
import org.singular.dto.util.RangeSerializer;

import java.util.List;

public class RangeDataset extends Dataset implements Comparable<RangeDataset> {
    @JsonSerialize(using = RangeSerializer.class)
    @JsonDeserialize(using = RangeDeserializer.class)
    private Range range;

    public RangeDataset() {
    }

    public RangeDataset(Range range, List<TagData> tagDatas) {
        this.range = range;
        this.dataset = tagDatas;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    @Override
    public int compareTo(RangeDataset o) {
        return this.range.compareTo(o.range);
    }
}
