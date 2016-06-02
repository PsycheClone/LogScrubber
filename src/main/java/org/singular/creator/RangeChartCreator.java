package org.singular.creator;

import javafx.util.Pair;
import org.joda.time.DateTime;
import org.singular.dto.TagData;
import org.singular.dto.RangeDataset;
import org.singular.dto.Range;
import org.singular.dto.util.TagDataAverageAscendingComparator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Component
public class RangeChartCreator extends AbstractCreator<RangeDataset> {

    @Override
    protected RangeDataset calculate() {
        RangeDataset rangeDataset = new RangeDataset();
        rangeDataset.setRange(new Range(new DateTime(header), new DateTime(header).plusMinutes(slice)));
        for(Map.Entry<String, List<Pair<DateTime, Long>>> metrics : metricsPerTag.entrySet()) {
            TagData tagData = new TagData();
            double average = 0;
            for(Pair<DateTime, Long> pair : metrics.getValue()) {
                average = average + pair.getValue();
            }
            average = average / metrics.getValue().size();

            tagData.setTag(metrics.getKey());
            tagData.setAverage(new BigDecimal(average / 1000).setScale(2, RoundingMode.HALF_UP).doubleValue());
            tagData.setCount(metrics.getValue().size());

            rangeDataset.addToDataset(tagData);
        }
        metricsPerTag.clear();
        rangeDataset.getDataset().sort(new TagDataAverageAscendingComparator());
        return rangeDataset;
    }
}
