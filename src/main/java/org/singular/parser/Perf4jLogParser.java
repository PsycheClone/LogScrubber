package org.singular.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.singular.entities.Perf4jLog;
import org.singular.entities.Perf4jLogLine;
import org.singular.entities.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Perf4jLogParser {

    @Autowired
    private ObjectMapper mapper;

    private Logger LOGGER = LoggerFactory.getLogger(Perf4jLogParser.class);

    private final String rootDir = System.getProperty("user.dir") + "/logs/";
    private final String logsDir = rootDir + "downloaded/";
    private DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd.HH-mm-ss");

    private final Pattern timestampPattern = Pattern.compile("\\d+-\\d+-\\d+\\s\\d+:\\d+:\\d+");

    public Perf4jLog processLogs(String perf4jLogs) throws IOException {
        String line;
        String start = "";
        String end = "";

        String [] logLines = perf4jLogs.split(System.getProperty("line.separator"));

        Perf4jLog log = new Perf4jLog();

        for(String logLine : Arrays.asList(logLines)) {
            if (logLine.startsWith("Performance Statistics")) {
                Matcher matcher = timestampPattern.matcher(logLine);
                if (matcher.find()) {
                    start = matcher.group(0);
                    start = start.replace(" ", "T");
                }
                if (matcher.find()) {
                    end = matcher.group(0);
                    end = end.replace(" ", "T");
                }
                log.setRange(new Range(new DateTime(start), new DateTime(end)));
            }
            LOGGER.debug("Perf4j timeslice: " + start + " - " + end);
            if (!logLine.startsWith("Performance Statistics") && !logLine.startsWith("Tag") && !logLine.equals("")) {
                List<String> values = Lists.newArrayList(logLine.split("\\s{2,}"));
                LOGGER.debug(values.toString());
                log.addLogLine(new Perf4jLogLine(values.get(0), parseDouble(values.get(1)),
                        Integer.parseInt(values.get(2)), Integer.parseInt(values.get(3)),
                        parseDouble(values.get(4)), Integer.parseInt(values.get(5))));
            }
        }

//        String json = mapper.writeValueAsString(log);
//        LOGGER.debug(json);
        return log;
    }

    private String createFileName(String start, String end) {
        return fmt.print(new DateTime(start)) + "," + fmt.print(new DateTime(end)) + ".log";
    }

    private Double parseDouble(String value) {
        if (value.contains(",")) {
            value = value.replace(",", ".");
        }
        return Double.parseDouble(value);
    }
}