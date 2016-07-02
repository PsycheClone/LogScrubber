package org.singular.slicer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.singular.BaseTest;
import org.singular.files.FileManager;
import org.singular.files.FileReader;
import org.singular.scrubber.TailSlicer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TailSlicerTest extends BaseTest {

    private TailSlicer tailSlicer = new TailSlicer();
    private FileManager fileManager = new FileManager();

    @Before
    public void before() {
        fileManager.setRootDir(testDir + "/slicer");
        tailSlicer.setHost("testHost");
        tailSlicer.setTimeslice(5);
        tailSlicer.setFileManager(fileManager);
    }

    @After
    public void after() throws IOException {
        List<File> files = fileManager.getFilteredFiles("testHost");
        for(File file : files) {
            file.delete();
        }
    }

    @Test
    public void sliceLogTest() throws IOException {
        String logs = FileReader.getContent(new File(testDir + "/slicer/tailerSlicerLoglines.log"));
        BufferedReader reader = new BufferedReader(new StringReader(logs));

        String line;
        while ((line = reader.readLine()) != null) {
            tailSlicer.process(line);
        }

        List<File> files = fileManager.getFilteredFiles("testHost");
        assertEquals(3, files.size());

        String firstLogStart = FileManager.getStartParsable(files.get(0).getName());
        String firstLogEnd = FileManager.getEndParsable(files.get(0).getName());
        assertEquals("2016-06-20T15:45:00", firstLogStart);
        assertEquals("2016-06-20T15:50:00", firstLogEnd);

        String secondLogStart = FileManager.getStartParsable(files.get(1).getName());
        String secondLogEnd = FileManager.getEndParsable(files.get(1).getName());
        assertEquals("2016-06-20T15:50:00", secondLogStart);
        assertEquals("2016-06-20T15:55:00", secondLogEnd);

        String thirdLogStart = FileManager.getStartParsable(files.get(2).getName());
        String thirdLogEnd = FileManager.getEndParsable(files.get(2).getName());
        assertEquals("2016-06-20T15:55:00", thirdLogStart);
        assertEquals("2016-06-20T16:00:00", thirdLogEnd);
    }
}
