package org.singular.parser.perf4jClasses;

import org.singular.parser.perf4jClasses.helper.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * LogParser provides the main method for reading a log of StopWatch output and generating statistics and graphs
 * from that output. Run "java -jar pathToPerf4jJar --help" for instructions.
 *
 * @author Alex Devine
 */
public class LogParser {
    /**
     * The input log that is being parsed.
     */
    private Reader inputLog;
    /**
     * The stream where the GroupedTimingStatistics data will be printed - if null, no statistics will be printed
     */
    private StringBuilder statisticsOutput;
    /**
     * The length of time, in milliseconds, of the timeslice of each GroupedTimingStatistics.
     */
    private long timeSlice;
    /**
     * Whether or not "rollup statistics" should be created for each GroupedTimingStatistics created.
     */
    private boolean createRollupStatistics;
    /**
     * The formatter to use to print statistics.
     */
    private GroupedTimingStatisticsFormatter statisticsFormatter;

    // --- Constructors ---
    /**
     * Default constructor reads input from standard in, writes statistics output to standard out, does not write
     * graph output, has a time process window of 30 seconds, and does not create rollup statistics.
     */

    /**
     * Creates a new LogParser to parse log data from the input.
     *
     * @param inputLog               The log being parsed, which should contain {@link org.singular.parser.perf4jClasses.StopWatch} log messages.
     * @param statisticsOutput       The stream where calculated statistics information should be written - if null,
     *                               statistics data is not written.
     * @param timeSlice              The length of time, in milliseconds, of the timeslice of each statistics data created.
     * @param createRollupStatistics Whether or not "rollup statistics" should be created for each timeslice of data.
     * @param statisticsFormatter    The formatter to use to print GroupedTimingStatistics
     */
    public LogParser(Reader inputLog, StringBuilder statisticsOutput,
                     long timeSlice, boolean createRollupStatistics,
                     GroupedTimingStatisticsFormatter statisticsFormatter) {
        this.inputLog = inputLog;
        this.statisticsOutput = statisticsOutput;
        this.timeSlice = timeSlice;
        this.createRollupStatistics = createRollupStatistics;
        this.statisticsFormatter = statisticsFormatter;
    }

    // --- Instance Methods ---

    /**
     * Reads all the data from the inputLog, parses it, and writes the statistics data and graphing data as desired
     * to the output streams.
     */
    public String parseLog() {

        Iterator<StopWatch> stopWatchIter = new StopWatchLogIterator(inputLog);

        int i = 0;
        for (GroupingStatisticsIterator statsIter = new GroupingStatisticsIterator(stopWatchIter,
                timeSlice,
                createRollupStatistics);
             statsIter.hasNext();) {
            GroupedTimingStatistics statistics = statsIter.next();

            if (statisticsOutput != null) {
                statisticsOutput.append(statisticsFormatter.format(statistics));
            }
        }
        return statisticsOutput.toString();
    }

    public static String runMain(String[] args) {
        String logs = "";
        try {
            List<String> argsList = new ArrayList<String>(Arrays.asList(args));

            StringBuilder statisticsOutput = new StringBuilder();
            long timeSlice = getTimeSlice(argsList);
            boolean rollupStatistics = getRollupStatistics(argsList);
            GroupedTimingStatisticsFormatter formatter = getStatisticsFormatter(argsList);
            Reader input = openInput(argsList);

            logs = new LogParser(input, statisticsOutput, timeSlice, rollupStatistics, formatter).parseLog();

            input.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return logs;
    }

    protected static PrintStream openStatisticsOutput(List<String> argsList) throws IOException {
        int indexOfOut = getIndexOfArg(argsList, true, "-o", "--output", "--out");
        if (indexOfOut >= 0) {
            String fileName = argsList.remove(indexOfOut + 1);
            argsList.remove(indexOfOut);
            return openStream(fileName);
        } else {
            return System.out;
        }
    }

    protected static long getTimeSlice(List<String> argsList) {
        int indexOfOut = getIndexOfArg(argsList, true, "-t", "--timeslice");
        if (indexOfOut >= 0) {
            String timeslice = argsList.remove(indexOfOut + 1);
            argsList.remove(indexOfOut);
            return Long.parseLong(timeslice);
        } else {
            return 30000L;
        }
    }

    protected static boolean getRollupStatistics(List<String> argsList) {
        int indexOfOut = getIndexOfArg(argsList, false, "-r", "--rollup");
        if (indexOfOut >= 0) {
            argsList.remove(indexOfOut);
            return true;
        } else {
            return false;
        }
    }

    protected static GroupedTimingStatisticsFormatter getStatisticsFormatter(List<String> argsList) {
        int indexOfFormat = getIndexOfArg(argsList, true, "-f", "--format");
        if (indexOfFormat >= 0) {
            String formatString = argsList.remove(indexOfFormat + 1);
            argsList.remove(indexOfFormat);
            if ("text".equalsIgnoreCase(formatString)) {
                return new GroupedTimingStatisticsTextFormatter();
            } else if ("csv".equalsIgnoreCase(formatString)) {
                return new GroupedTimingStatisticsCsvFormatter();
            } else {
                throw new IllegalArgumentException("Unknown format type: " + formatString);
            }
        } else {
            return new GroupedTimingStatisticsTextFormatter();
        }
    }

    protected static Reader openInput(List<String> argsList) throws IOException {
        if (argsList.isEmpty()) {
            return new InputStreamReader(System.in);
        } else {
            String fileName = argsList.remove(0);
            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileName.getBytes())));
        }
    }

    protected static int getIndexOfArg(List<String> args, boolean needsParam, String... argNames) {
        int retVal = -1;
        boolean foundArg = false;
        for (String argName : argNames) {
            int argIndex = args.indexOf(argName);
            if (argIndex >= 0) {
                if (foundArg) {
                    throw new IllegalArgumentException("You must specify only one of " + Arrays.toString(argNames));
                }
                retVal = argIndex;
                foundArg = true;
            }
        }

        if ((retVal >= 0) && needsParam && (retVal == args.size() - 1)) {
            throw new IllegalArgumentException("You must specify a parameter for the " + args.get(retVal) + " arg");
        }

        return retVal;
    }

    protected static PrintStream openStream(String fileName) throws IOException {
        if ("stdout".equals(fileName) || "out".equals(fileName)) {
            return System.out;
        } else if ("stderr".equals(fileName) || "err".equals(fileName)) {
            return System.err;
        } else {
            return new PrintStream(new FileOutputStream(fileName), true);
        }
    }
}
