package org.singular.scrubber;

import org.joda.time.DateTime;
import org.singular.files.FileManager;
import org.singular.parser.LogParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

@Component("slicer")
@Scope("prototype")
public class Slicer {

    @Value("${timeslice}")
    private int timeslice;

    private LogParser logParser = new LogParser();

    @Autowired
    private FileManager fileManager;

    private boolean tailer;
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
            start = nearestXMinutes(new DateTime(logParser.getTimestamp(line)));
            timer = new DateTime();
            reset = false;
        }

        end = new DateTime(logParser.getTimestamp(line));

        if(end.isAfter(start.plusMinutes(timeslice))) {
            takeSlice();
            reservoir.clear();
            reservoir.add(line);
            reset = true;
        } else if(new DateTime().isAfter(timer.plusMinutes(timeslice))) {
            LOGGER.info(host + " slicer timed out...");
            timer = new DateTime();
        } else {
            reservoir.add(line);
            LOGGER.debug(line);
        }
    }

    private void takeSlice() throws FileNotFoundException, UnsupportedEncodingException {
        String logsToStore = formatLogs();
        String fileName = createFileName();
        fileManager.storeFile(fileName, logsToStore);
        LOGGER.info("Storing file for " + host + ": " + start + " - " + start.plusMinutes(timeslice));
    }

    private String formatLogs() {
        StringBuilder stringBuilder = new StringBuilder();
        for(String line : reservoir) {
            stringBuilder.append(line + "\n");
        }
        return stringBuilder.toString();
    }

    public DateTime nearestXMinutes(DateTime time) {
        int subtractMinutes = time.getMinuteOfHour()%timeslice;
        int subtractSeconds = time.getSecondOfMinute();
        int subtractMillisecs = time.getMillisOfSecond();

        int subtractTotal = (((subtractMinutes * 60) + subtractSeconds) * 1000) + subtractMillisecs;

        return new DateTime(time.minus(subtractTotal));
    }

    private String createFileName() {
        return host + "_" + start.getMillis() + "_" + start.plusMinutes(timeslice).getMillis() + ".log";
    }

    public boolean isTailer() {
        return tailer;
    }

    public void setTailer(boolean tailer) {
        this.tailer = tailer;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getTimeslice() {
        return timeslice;
    }

    public void setTimeslice(int timeslice) {
        this.timeslice = timeslice;
    }
}
