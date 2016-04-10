package org.singular;

import org.joda.time.DateTime;
import org.junit.Test;
import org.singular.logDistribution.Slicer;

public class SlicerTest extends BaseTest {

    private Slicer slicer = new Slicer();

    @Test
    public void sliceLogTest() {
        DateTime time = new DateTime("2016-04-10T11:43:12");
        DateTime subtracted = slicer.nearest5Minutes(time);
        System.out.print(subtracted);
    }
}
