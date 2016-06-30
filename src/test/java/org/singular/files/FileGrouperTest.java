package org.singular.files;

import org.joda.time.DateTime;
import org.junit.Test;
import org.singular.BaseTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FileGrouperTest extends BaseTest {

    private FileManager fileManager = new FileManager();

    @Test
    public void groupListIn1GroupTest() {
        List<File> files = new ArrayList<File>();
        files.add(new File(testDir + "test/testHost_1460785800000_1460786100000.log"));
        files.add(new File(testDir + "test/testHost_1460786100000_1460786400000.log"));
        List<List<File>> groupedFiles = FileGrouper.groupBySlice(files, 10);
        assertEquals(1, groupedFiles.size());
    }

    @Test
    public void groupListIn2GroupsTest() {
        List<File> files = new ArrayList<File>();
        files.add(new File(testDir + "test/testHost_1460785800000_1460786100000.log"));
        files.add(new File(testDir + "test/testHost_1460786100000_1460786400000.log"));
        List<List<File>> groupedFiles = FileGrouper.groupBySlice(files, 5);
        assertEquals(2, groupedFiles.size());
    }

    @Test
    public void groupListIn2GroupsWithRestTest() {
        List<File> files = new ArrayList<File>();
        files.add(new File(testDir + "test/testHost_1460785800000_1460786100000.log"));
        files.add(new File(testDir + "test/testHost_1460786100000_1460786400000.log"));
        files.add(new File(testDir + "test/testHost_1460786400000_1460786700000.log"));
        List<List<File>> groupedFiles = FileGrouper.groupBySlice(files, 10);
        assertEquals(2, groupedFiles.size());
    }

    @Test
    public void groupLargeList() {
        fileManager.setRootDir(testDir + "fileGrouper");
        List<File> files = fileManager.getFilteredFilesWithinRange("APE", new DateTime("2016-06-01"), new DateTime("2016-06-30"));
        List<List<File>> groupedFiles = FileGrouper.groupBySlice(files, 10);
        assertEquals(27, groupedFiles.size());
    }
}
