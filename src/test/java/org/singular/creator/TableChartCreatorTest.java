package org.singular.creator;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.singular.BaseTest;
import org.singular.DataFactory;
import org.singular.dto.Range;
import org.singular.dto.RangeDataset;
import org.singular.files.FileManager;
import org.singular.files.FileReader;
import org.singular.parser.LogParser;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TableChartCreatorTest extends BaseTest {

    private TableChartCreator tableChartCreator = new TableChartCreator();

    private FileManager fileManager = new FileManager();

    private DataFactory dataFactory = new DataFactory();

    @Before
    public void before() {
        fileManager.setRootDir(testDir + "/creator");
        tableChartCreator.setFileManager(fileManager);
        tableChartCreator.setTimeslice(5);
    }

    @Test
    public void createTablechartTest() throws IOException {
        RangeDataset barchartLog = tableChartCreator.create("testHost", new DateTime("2016-04-16T07:50:00"), new DateTime("2016-04-16T08:00:00"), 5).get(0);
        assertEquals(barchartLog.getRange(), (new Range(new DateTime("2016-04-16T07:50:00"), new DateTime("2016-04-16T08:00:00"))));
        assertEquals(barchartLog.getDataset().size(), 7);
    }
}
