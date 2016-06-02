package org.singular.dto.util;

import org.singular.dto.TagData;

import java.util.Comparator;

public class TagDataAverageAscendingComparator implements Comparator<TagData> {
    @Override
    public int compare(TagData d1, TagData d2) {
        return d1.getAverage() == d2.getAverage() ? 0 : d1.getAverage() < d2.getAverage() ? -1 : 1;
    }
}
