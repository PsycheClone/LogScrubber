package org.singular.scrubber;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.singular.fileManager.FileManager;
import org.singular.parser.MelexisLogParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component("slicer")
@Scope("prototype")
public class Slicer {

    private int timeslice = 5;

    @Autowired
    private MelexisLogParser melexisLogParser;

    @Autowired
    private FileManager fileManager;

    private boolean reset = true;
    private String host;
    private DateTime start;
    private DateTime end;
    private DateTime timer;
    private List<String> reservoir = new LinkedList<String>();

    private Logger LOGGER = LoggerFactory.getLogger(Slicer.class);

    public Slicer() {
    }

    public void process(String line) throws IOException {
        if(reset) {
            start = nearest5Minutes(new DateTime(melexisLogParser.getTimestamp(line)));
            timer = new DateTime();
            reset = false;
        }

        end = new DateTime(melexisLogParser.getTimestamp(line));

        if(end.isBefore(start.minusMinutes(timeslice))) {
            takeSlice();
            reset = true;
        } else if(new DateTime().isAfter(timer.plusMinutes(5))) {
            LOGGER.info(host + " slicer timed out...");
            timer = new DateTime();
        } else {
            reservoir.add(line);
        }
    }

    public void takeSlice() throws FileNotFoundException, UnsupportedEncodingException {
        String logsToStore = formatLogs();
        String fileName = createFileName();
        fileManager.storeFile(fileName, logsToStore);
        LOGGER.info("Storing file for " + host + ": " + start + " - " + start.plusMinutes(timeslice));
        reservoir = Lists.newArrayList(reservoir.get(0));
    }

    private String formatLogs() {
        StringBuilder stringBuilder = new StringBuilder();
        Collections.reverse(reservoir);
        for(String line : reservoir.size() > 1 ? reservoir.subList(1, reservoir.size() - 1) : reservoir) {
            stringBuilder.append(line + "\"");
        }
        return stringBuilder.toString();
    }

    public DateTime nearest5Minutes(DateTime time) {
        int subtractMinutes = time.getMinuteOfHour()%timeslice;
        int subtractSeconds = time.getSecondOfMinute();
        int subtractMillisecs = time.getMillisOfSecond();

        int subtractTotal = (((subtractMinutes * 60) + subtractSeconds) * 1000) + subtractMillisecs;

        return new DateTime(time.minus(subtractTotal).plusMinutes(timeslice));
    }

    private String createFileName() {
        return host + "_" + nearest5Minutes(end).getMillis() + "_" + start.getMillis();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
