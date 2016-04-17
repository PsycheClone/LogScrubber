package org.singular.parser;

import org.joda.time.DateTime;
import org.singular.dto.LogLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LogParser {

    private Logger LOGGER = LoggerFactory.getLogger(LogParser.class);

    private Pattern startPattern = Pattern.compile("(start\\[)(.*?)(\\])");
    private Pattern timePattern = Pattern.compile("(time\\[)(.*?)(\\])");
    private Pattern tagPattern = Pattern.compile("(tag\\[)(.*?)(\\])");
    private final Pattern timestampPattern = Pattern.compile("\\d+-\\d+-\\d+\\s\\d+:\\d+:\\d+");

    public List<LogLine> parseLogs(String logs) throws IOException {
        List<LogLine> logLines = new ArrayList<LogLine>();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(logs.getBytes())));
        String line;
        while((line = bufferedReader.readLine()) != null) {
            logLines.add(parseLogLine(line));
        }
        bufferedReader.close();
        return logLines;
    }

    private LogLine parseLogLine(String line) {
        LogLine logLine = new LogLine();
        Matcher matcher = startPattern.matcher(line);
        if(matcher.find()) {
            logLine.setStart(new DateTime(Long.valueOf(matcher.group(2))));
        }
        matcher = timePattern.matcher(line);
        if(matcher.find()) {
            logLine.setDuration(Long.valueOf(matcher.group(2)));
        }
        matcher = tagPattern.matcher(line);
        if(matcher.find()) {
            logLine.setTag(matcher.group(2));
        }
        LOGGER.debug("Parsed logline: " + logLine);
        return logLine;
    }

    public String getTimestamp(String line) {
        Matcher matcher = timestampPattern.matcher(line);
        if(matcher.find()) {
            return matcher.group().replace(" ", "T");
        }
        return null;
    }
}
