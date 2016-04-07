package org.singular;

import org.junit.Test;
import org.singular.parser.Perf4jLogParser;

import java.io.File;
import java.io.IOException;

public class Perf4jParserTest {
    String rootDir = System.getProperty("user.dir") + "/src/test/resources/";
    private Perf4jLogParser perf4jParser = new Perf4jLogParser();

    @Test
    public void testParse() throws IOException {
        perf4jParser.processLogs(new File(rootDir + "logExample.txt"));
    }
}

//Performance Statistics   2016-03-17 11:30:00 - 2016-03-17 12:00:00
//Tag                                                  Avg(ms)         Min         Max     Std Dev       Count
//    ::: Dashboard/HOS/OfflPDev :::                        1715.3        1475        2347       323.6           6
//    ::: Dashboard/PDS/availabilityByMachine) :::             7.7           6          15         3.3           6
//    ::: Dashboard/PDS/availabilityByMachineAndDevice :::       446.2         360         573        71.3           6
//    ::: Dashboard/PDS/availabilityForDevices :::           459.5         370         583        71.1           6
//    ::: Dashboard/PDS/averageByDevice :::                    1.7           1           4         1.1           6
//    ::: Dashboard/PDS/averageByDeviceAndMachine :::          1.3           0           3         0.9           6
//    ::: Dashboard/PDS/findMachines :::                       4.2           3           5         0.7           6
//    ::: Dashboard/PDS/latest(devices) :::                    8.2           6          12         2.0           6
//    ::: Dashboard/ProcessorInternal :::                   2207.8        1872        2875       338.3           6
//    ::: Dashboard/addFullyOfflineHandlers :::             1717.0        1477        2349       324.0           6
//    ::: TskPoller End Of Poll :::                          161.2         120         194        22.7           6
//    ::: TskPoller getEventPreviousReport :::                 2.1           1           9         1.7          42
//    ::: TskPoller getEvents :::                              3.9           2          30         3.4         178
//    ActiveMQ2ConnectionExtension/createConnection            6.2           0         185        13.3        1362
//    ActiveMQ2ConnectionFactoryExtension/createConnection         5.2           0          96        10.0        1362
//    DateTimeSeriesAggregator/findPrevious                  318.8          14        5932       429.1         566
//    MultitestBootstrapper/bootstrap                         22.0           6          75        20.9           8
//    MultitestStateMachine/processEvent                       1.4           0          37         4.4         596
//    ReportStatisticsExtractor/extractAvailability            1.7           0          33         4.8          55
//    ReportStatisticsExtractor/extractJams                    1.9           0          11         2.4          46
//    ReportStatisticsExtractor/extractThroughput              0.3           0           3         0.6          55
//    StatisticsAggregator/findPrevious                      334.8          14        3545       386.9        1381
