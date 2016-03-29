package org.singular.parser;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.singular.fileManager.FileManager;
import org.singular.parser.perf4jClasses.LogParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MelexisLogParser {

    @Autowired
    public FileManager fileManager;

    private Logger LOGGER = LoggerFactory.getLogger(MelexisLogParser.class);

    private final String rootDir = System.getProperty("user.dir") + "/logs/";
    private final String perfDir = rootDir + "perf4j/";

    private final Pattern timestampPattern = Pattern.compile("\\d+-\\d+-\\d+\\s\\d+:\\d+:\\d+");

    public void handleRawLogs(final File file) throws InterruptedException, IOException {
        List<String> loglines = fillList(file);
        long largestTimeSlice = getLargestPossibleTimeSlice(loglines);
        LOGGER.info("Larget Possible Timeslice for " + file.getName() + ": " + largestTimeSlice + " minutes.");
        for(String [] args : createOutputFileNames(file, largestTimeSlice)) {
            LogParser.runMain(args);
        }
    }

    private List<String> fillList(File file) throws IOException {
        List<String> loglines = new LinkedList<String>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line;
        while((line = bufferedReader.readLine()) != null) {
            loglines.add(line);
        }
        bufferedReader.close();
        return loglines;
    }

    private List<String []> createOutputFileNames(File file, long largestTimeSlice) {
        List<String []> argsForTimeSlices = new ArrayList<String[]>();
        largestTimeSlice
        String outputFile = outputFileName(file, 30);
        String[] args = {file.getAbsolutePath(), "-t", toLong(30), "-o", outputFile};
    }

    private long getLargestPossibleTimeSlice(List<String> loglines) {
        DateTime earliest = new DateTime(getTimestamp(loglines.get(0)));
        DateTime latest = new DateTime(getTimestamp(loglines.get(loglines.size()-1)));
        Duration duration = new Duration(earliest, latest);
        return duration.getStandardMinutes();
    }

    public String getTimestamp(String line) {
        Matcher matcher = timestampPattern.matcher(line);
        if(matcher.find()) {
            return matcher.group().replace(" ", "T");
        }
        return null;
    }

    private String outputFileName(File file, int slice) {
        String fileName = file.getName().replace("done", "Timeslice-" + slice + "minutes");
        fileManager.createFolderIfNotExists(perfDir);
        return perfDir + fileName;
    }

    private String toLong(int slice) {
        return Long.toString(slice * 60000);
    }
}
