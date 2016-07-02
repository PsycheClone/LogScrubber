package org.singular.calculate;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.singular.BaseTest;
import org.singular.dto.*;
import org.singular.files.FileManager;
import org.singular.files.FileReader;
import org.singular.parser.LogParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CalculateTest extends BaseTest {

    private FileManager fileManager = new FileManager();

    private Calculator calculator = new Calculator();

    private RangeDataset rangeDataset50to55 = new RangeDataset();

    @Before
    public void before() {
        rangeDataset50to55.setRange(new Range(new DateTime("2016-06-20T15:50"), new DateTime("2016-06-20T15:55")));
        TagData data = new TagData("::: Dashboard/DSL/latestDevices :::", 4, 6, 5.0, 1.0, 2);
        rangeDataset50to55.addToDataset(data);
        data = new TagData("::: Dashboard/DSL/latestMachines :::", 4, 5, 4.5, 0.5, 2);
        rangeDataset50to55.addToDataset(data);
        data = new TagData("::: Dashboard/DeviceStatusesProcessor/addFullyOfflineHandlers.getAllMachineOfflineInfoPerDevice", 3, 14, 8.5, 5.5, 2);
        rangeDataset50to55.addToDataset(data);
        data = new TagData("::: Dashboard/HOS/OfflPDev :::", 3, 14, 8.5, 5.5, 2);
        rangeDataset50to55.addToDataset(data);
        data = new TagData("::: Dashboard/ProcessInternal.getCurrentMachinesForTag :::", 0, 0, 0.0, 0.0, 2);
        rangeDataset50to55.addToDataset(data);
        data = new TagData("::: Dashboard/ProcessInternalHANDLERS/availabilityService.getDeviceStatusList :::", 12, 14, 13.0, 1.0, 2);
        rangeDataset50to55.addToDataset(data);
        data = new TagData("::: Dashboard/ProcessInternalHANDLERS/offlineService.addFullyOfflineHandlers :::", 3, 14, 8.5, 5.5, 2);
        rangeDataset50to55.addToDataset(data);
        data = new TagData("::: Dashboard/addFullyOfflineHandlers :::", 3, 14, 8.5, 5.5, 2);
        rangeDataset50to55.addToDataset(data);
        data = new TagData("::: TskPoller End Of Poll :::", 168, 168, 168.0, 0.0, 1);
        rangeDataset50to55.addToDataset(data);
        data = new TagData("::: TskPoller getEventPreviousReport :::", 2, 3, 2.7, 0.5, 6);
        rangeDataset50to55.addToDataset(data);
        data = new TagData("DateTimeSeriesAggregator/findPrevious", 4, 285, 34.6, 42.5, 66);
        rangeDataset50to55.addToDataset(data);
        data = new TagData("ReportStatisticsExtractor/extractAvailability", 0, 12, 1.2, 2.3, 32);
        rangeDataset50to55.addToDataset(data);
        data = new TagData("ReportStatisticsExtractor/extractThroughput", 0, 18, 1.2, 3.1, 32);
        rangeDataset50to55.addToDataset(data);
        data = new TagData("StatisticsAggregator/findPrevious", 3, 446, 33.6, 51.7, 96);
        rangeDataset50to55.addToDataset(data);
    }

    @Test
    public void calculate1FileTest() throws IOException {
        RangeDataset rangeDataset = new RangeDataset();
        fileManager.setRootDir(testDir + "calculate");
        List<File> files = fileManager.getFilteredFilesWithinRange("APE", new DateTime("2016-06-20T15:50"), new DateTime("2016-06-20T15:55"));
        String logs = FileReader.getContent(files);
        List<LogLine> logLines = LogParser.parseLogs(logs);
        calculator.calculateAndAdd(logLines, rangeDataset);
        for(TagData tagData : (List<TagData>)rangeDataset.getDataset()) {
            for(TagData tagDataToTest : (List<TagData>)rangeDataset50to55.getDataset()) {
                if(tagDataToTest.getTag().equals(tagData.getTag())) {
                    assertEquals(tagDataToTest.getAverage(), tagData.getAverage());
                    assertEquals(tagDataToTest.getMax(), tagData.getMax());
                    assertEquals(tagDataToTest.getMin(), tagData.getMin());
                    assertEquals(tagDataToTest.getStdDev(), tagData.getStdDev());
                    assertEquals(tagDataToTest.getCount(), tagData.getCount());
                }
            }
        }
    }

    // Perf4j jar results for file calculate/APE_1466430600000_1466430900000.log
//      Tag                                                                                                 Avg(ms)         Min         Max     Std Dev       Count
//    ::: Dashboard/DSL/latestDevices :::                                                                     5.0           4           6         1.0           2
//    ::: Dashboard/DSL/latestMachines :::                                                                    4.5           4           5         0.5           2
//    ::: Dashboard/DeviceStatusesProcessor/addFullyOfflineHandlers.getAllMachineOfflineInfoPerDevice         8.5           3          14         5.5           2
//    ::: Dashboard/HOS/OfflPDev :::                                                                          8.5           3          14         5.5           2
//    ::: Dashboard/ProcessInternal.getCurrentMachinesForTag :::                                              0.0           0           0         0.0           2
//    ::: Dashboard/ProcessInternalHANDLERS/availabilityService.getDeviceStatusList :::                      13.0          12          14         1.0           2
//    ::: Dashboard/ProcessInternalHANDLERS/offlineService.addFullyOfflineHandlers :::                        8.5           3          14         5.5           2
//    ::: Dashboard/addFullyOfflineHandlers :::                                                               8.5           3          14         5.5           2
//    ::: TskPoller End Of Poll :::                                                                         168.0         168         168         0.0           1
//    ::: TskPoller getEventPreviousReport :::                                                                2.7           2           3         0.5           6
//    DateTimeSeriesAggregator/findPrevious                                                                  34.6           4         285        42.5          66
//    ReportStatisticsExtractor/extractAvailability                                                           1.2           0          12         2.3          32
//    ReportStatisticsExtractor/extractThroughput                                                             1.2           0          18         3.1          32
//    StatisticsAggregator/findPrevious                                                                      33.6           3         446        51.7          96
}
