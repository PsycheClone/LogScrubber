package org.singular.slicer;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.singular.BaseTest;
import org.singular.files.*;
import org.singular.parser.LogParser;
import org.singular.scrubber.ReverseSlicer;
import org.singular.scrubber.TailSlicer;

import java.io.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ReverseSlicerTest extends BaseTest {

    private ReverseSlicer reverseSlicer = new ReverseSlicer();
    private FileManager fileManager = new FileManager();

    @Before
    public void before() {
        fileManager.setRootDir(testDir + "/slicer");
        reverseSlicer.setHost("testHost");
        reverseSlicer.setTimeslice(5);
        reverseSlicer.setFileManager(fileManager);
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
        String logs = org.singular.files.FileReader.getContent(new File(testDir + "/slicer/reverseSlicerLoglines.log"));
        BufferedReader reader = new BufferedReader(new StringReader(logs));

        String line;
        while ((line = reader.readLine()) != null) {
            reverseSlicer.process(line);
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
