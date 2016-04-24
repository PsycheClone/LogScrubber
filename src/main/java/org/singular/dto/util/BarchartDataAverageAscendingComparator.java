package org.singular.dto.util;

import org.singular.dto.BarchartData;

import java.util.Comparator;

public class BarchartDataAverageAscendingComparator implements Comparator<BarchartData> {
    @Override
    public int compare(BarchartData d1, BarchartData d2) {
        return d1.getAverage() == d2.getAverage() ? 0 : d1.getAverage() < d2.getAverage() ? -1 : 1;
    }
}
