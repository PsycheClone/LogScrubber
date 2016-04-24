package org.singular.creator;

import javafx.util.Pair;
import org.joda.time.DateTime;
import org.singular.dto.BarchartData;
import org.singular.dto.BarchartDataset;
import org.singular.dto.Range;
import org.singular.dto.util.BarchartDataAverageAscendingComparator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Component
public class BarchartCreator extends AbstractCreator<BarchartDataset> {

    @Override
    protected BarchartDataset calculate() {
        BarchartDataset barchartDataset = new BarchartDataset();
        barchartDataset.setRange(new Range(new DateTime(header), new DateTime(header).plusMinutes(slice)));
        for(Map.Entry<String, List<Pair<DateTime, Long>>> metrics : metricsPerTag.entrySet()) {
            BarchartData barchartData = new BarchartData();
            double average = 0;
            for(Pair<DateTime, Long> pair : metrics.getValue()) {
                average = average + pair.getValue();
            }
            average = average / metrics.getValue().size();

            barchartData.setTag(metrics.getKey());
            barchartData.setAverage(new BigDecimal(average / 1000).setScale(2, RoundingMode.HALF_UP).doubleValue());
            barchartData.setCount(metrics.getValue().size());

            barchartDataset.addToDataset(barchartData);
        }
        metricsPerTag.clear();
        barchartDataset.getDataset().sort(new BarchartDataAverageAscendingComparator());
        return barchartDataset;
    }
}
