package org.singular.creator;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.singular.BaseTest;
import org.singular.DataFactory;
import org.singular.dto.BarchartDataset;
import org.singular.dto.Range;
import org.singular.files.FileManager;
import org.singular.files.FileReader;
import org.singular.parser.LogParser;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class BarchartCreatorTest extends BaseTest {

    @InjectMocks
    private BarchartCreator barchartCreator;

    @Mock
    private FileManager fileManager;

    @Mock
    private FileReader fileReader;

    @Mock
    private LogParser logParser;

    private DataFactory dataFactory = new DataFactory();

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createBarchartTest() throws IOException {
        File file = new File(testDir + "loglines4Test.log");
        when(fileManager.getFilteredFilesWithinRange("testHost", "2016-04-16T08:30:00", 15)).thenReturn(Lists.newArrayList(file));
        when(fileReader.getContent(fileManager.getFilteredFilesWithinRange("testHost", "2016-04-16T08:30:00", 15))).thenReturn(getContent(file));

        when(logParser.parseLogs(getContent(file))).thenReturn(dataFactory.getLoglines());

        BarchartDataset barchartLog = barchartCreator.create("testHost", "2016-04-16T08:30:00", "2016-04-16T08:30:00", 15);
        assertEquals(barchartLog.getRange(), (new Range(new DateTime("2016-04-16T08:30:00"), new DateTime("2016-04-16T08:45:00"))));
        assertEquals(barchartLog.getDataset().size(), 7);
    }
}
