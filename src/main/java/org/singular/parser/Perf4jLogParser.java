package org.singular.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.singular.entities.Log;
import org.singular.entities.LogLine;
import org.singular.entities.Range;
import org.singular.fileManager.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Perf4jLogParser {

    @Autowired
    private FileManager fileManager;

    @Autowired
    private ObjectMapper mapper;

    private Logger LOGGER = LoggerFactory.getLogger(Perf4jLogParser.class);

    private final String rootDir = System.getProperty("user.dir") + "/logs/";
    private final String logsDir = rootDir + "downloaded/";
    private DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd.HH-mm-ss");

    private final Pattern timestampPattern = Pattern.compile("\\d+-\\d+-\\d+\\s\\d+:\\d+:\\d+");

    public void processLogs(File file) throws IOException {
        Set<Log> logs = new TreeSet<Log>();
        String line;
        String start = "";
        String end = "";

        BufferedReader reader = new BufferedReader(new FileReader(file));

        Log log = new Log();

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Performance Statistics")) {
                if (log.getRange() != null) {
                    logs.add(log);
                }
                log = new Log();
                Matcher matcher = timestampPattern.matcher(line);
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
            if (!line.startsWith("Performance Statistics") && !line.startsWith("Tag") && !line.equals("")) {
                List<String> values = Lists.newArrayList(line.split("\\s{2,}"));
                LOGGER.debug(values.toString());
                log.addLogLine(new LogLine(values.get(0), parseDouble(values.get(1)),
                        Integer.parseInt(values.get(2)), Integer.parseInt(values.get(3)),
                        parseDouble(values.get(4)), Integer.parseInt(values.get(5))));
            }
        }
        reader.close();

        String json = mapper.writeValueAsString(log);
        LOGGER.debug(json);
        fileManager.storeFile(createFileName(start, end), json);
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