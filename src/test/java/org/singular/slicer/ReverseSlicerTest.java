package org.singular.slicer;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.singular.BaseTest;
import org.singular.files.FileManager;
import org.singular.parser.LogParser;
import org.singular.scrubber.ReverseSlicer;

import java.io.*;

import static org.mockito.Mockito.*;

@Ignore
public class ReverseSlicerTest extends BaseTest {

    @InjectMocks
    private ReverseSlicer slicer;

    @Mock
    private LogParser logParser;

    @Mock
    private FileManager fileManager;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        slicer.setHost("testHost");
        slicer.setTailer(false);
        slicer.setTimeslice(5);
    }

    @Test
    public void sliceLogTest() throws IOException {
        File file = new File(testDir + "loglines4ReverseTest.log");
        String firstFileName = "testHost_1460785800000_1460786100000.log";
        File firstPartToTest = new File(testDir + "slicer/reverse/slicerReverseTestFirstPart.log");
        String secondFileName = "testHost_1460786100000_1460786400000.log";
        File secondPartToTest = new File(testDir + "slicer/reverse/slicerReverseTestSecondPart.log");
        String content = getContent(file);

        when(logParser.getTimestamp("2016-04-16 07:59:31,883 | INFO  | qtp914170664-517 | TimingLogger                     | 72 - org.apache.servicemix.bundles.perf4j - 0.9.13.1 | start[1460786191555] time[328] tag[::: Dashboard/DSL/AvailabilityByDeviceAndMachine :::]"))
                .thenReturn("2016-04-16T07:59:31");

        when(logParser.getTimestamp("2016-04-16 07:49:37,230 | INFO  | xtenderThread-32 | TimingLogger                     | 72 - org.apache.servicemix.bundles.perf4j - 0.9.13.1 | start[1460785897220] time[10] tag[To Complete the slice]"))
                .thenReturn("2016-04-16T07:49:37");
        when(logParser.getTimestamp("2016-04-16 07:51:37,230 | INFO  | xtenderThread-32 | TimingLogger                     | 72 - org.apache.servicemix.bundles.perf4j - 0.9.13.1 | start[1460785897220] time[10] tag[ActiveMQ2ConnectionFactoryExtension/createConnection]"))
                .thenReturn("2016-04-16T07:51:37");
        when(logParser.getTimestamp("2016-04-16 07:52:37,231 | INFO  | xtenderThread-32 | TimingLogger                     | 72 - org.apache.servicemix.bundles.perf4j - 0.9.13.1 | start[1460785897220] time[11] tag[ActiveMQ2ConnectionFactoryExtension/createConnection]"))
                .thenReturn("2016-04-16T07:52:37");
        when(logParser.getTimestamp("2016-04-16 07:53:37,247 | INFO  | handled.ape.pnt] | TimingLogger                     | 72 - org.apache.servicemix.bundles.perf4j - 0.9.13.1 | start[1460785897242] time[5] tag[ActiveMQ2ConnectionExtension/createConnection]"))
                .thenReturn("2016-04-16T07:53:37");
        when(logParser.getTimestamp("2016-04-16 07:54:45,015 | INFO  | ovetransactions] | TimingLogger                     | 72 - org.apache.servicemix.bundles.perf4j - 0.9.13.1 | start[1460785905004] time[11] tag[ActiveMQ2ConnectionExtension/createDoodoo]"))
                .thenReturn("2016-04-16T07:54:45");

        when(logParser.getTimestamp("2016-04-16 07:56:31,531 | INFO  | qtp914170664-517 | TimingLogger                     | 72 - org.apache.servicemix.bundles.perf4j - 0.9.13.1 | start[1460786191497] time[34] tag[::: Dashboard/DSL/latestDevices :::]"))
                .thenReturn("2016-04-16T07:56:31");
        when(logParser.getTimestamp("2016-04-16 07:58:31,555 | INFO  | qtp914170664-517 | TimingLogger                     | 72 - org.apache.servicemix.bundles.perf4j - 0.9.13.1 | start[1460786191531] time[24] tag[::: Dashboard/DSL/latestMachines :::]"))
                .thenReturn("2016-04-16T07:58:31");


        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes())));
        String line;
        while((line = bufferedReader.readLine()) != null) {
            slicer.process(line);
        }

        verify((fileManager), times(1)).storeFile(secondFileName, getContent(secondPartToTest));
        verify((fileManager), times(1)).storeFile(firstFileName, getContent(firstPartToTest));
    }
}
