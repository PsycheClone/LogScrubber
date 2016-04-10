package org.singular.logDistribution;

import org.apache.camel.Message;
import org.joda.time.DateTime;
import org.singular.fileManager.FileManager;
import org.singular.parser.MelexisLogParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class Slicer {

    @Autowired
    private MelexisLogParser melexisLogParser;

    @Autowired
    private FileManager fileManager;

    private List<String> reservoir = new LinkedList<String>();
    private Map<String, List<String>> buffer = new HashMap<String, List<String>>();

    private Logger LOGGER = LoggerFactory.getLogger(Slicer.class);

    public void process(Message message) throws IOException {
        InputStream stream = new ByteArrayInputStream(message.getBody().toString().getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String host = message.getHeader("id").toString();

        String line;

        while((line = reader.readLine()) != null) {
            reservoir.add(line);
        }

        slice(host);
    }

    public void slice(String host) throws FileNotFoundException, UnsupportedEncodingException {
        List<String> logs = new LinkedList<String>();
        DateTime startTimestamp = new DateTime(melexisLogParser.getTimestamp(reservoir.get(0)));
        DateTime start = nearest5Minutes(startTimestamp);
        DateTime timer = new DateTime();
        DateTime end;
        for(String logLine : reservoir) {
            end = new DateTime(melexisLogParser.getTimestamp(logLine));
            if(end.isAfter(start.plusMinutes(5))) {
                String logsToStore = formatLogs(logs);
                fileManager.storeFile("yes", host+"_"+start.getMillis()+"_"+new DateTime(melexisLogParser.getTimestamp(logs.get(logs.size()-1))).getMillis(), logsToStore);
                LOGGER.info("Storing file for " + host + ": " + start + " - " + melexisLogParser.getTimestamp(logs.get(logs.size()-1)));
                start = nearest5Minutes(end);
                timer = new DateTime();
                logs.clear();
            } else if(new DateTime().isAfter(timer.plusSeconds(5))) {
                LOGGER.info("Timeout Slicer...");
                buffer.put(host, logs);
                LOGGER.info(buffer.toString());
                timer = new DateTime();
            } else {
                logs.add(logLine);
            }
        }
        reservoir.clear();
    }

    private String formatLogs(List<String> logs) {
        StringBuilder stringBuilder = new StringBuilder();
        for(String line : logs) {
            stringBuilder.append(line + "\"");
        }
        return stringBuilder.toString();
    }

    public DateTime nearest5Minutes(DateTime time) {
        int subtractMinutes = time.getMinuteOfHour()%5;
        int subtractSeconds = time.getSecondOfMinute();
        int subtractMillisecs = time.getMillisOfSecond();

        int subtractTotal = (((subtractMinutes * 60) + subtractSeconds) * 1000) + subtractMillisecs;

        return new DateTime(time.minus(subtractTotal));
    }
}
