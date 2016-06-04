package org.singular;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;

public class LogCreatorMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        String templateLog = "{0} | time[{1}] tag[{2}]\n";
        String fileName = System.getProperty("user.home") + "/.logscrubber/testLog.log";
        File logFile = new File(fileName);
        logFile.createNewFile();
        int minutes = 0;
        while(true) {
            DateTime time = new DateTime();
            String duration = String.valueOf((int)(Math.random()*1500)).replaceAll(",", "");
            int tagNumber = (int)(Math.random()*3);
            String tag = "This is tag number " + tagNumber;
            String timeFormatted = time.plusMinutes(++minutes).toLocalDateTime().toString();
            timeFormatted = timeFormatted.replace("T", " ");
            timeFormatted = timeFormatted.replace(".", ",");
            Files.write(Paths.get(fileName), MessageFormat.format(templateLog, timeFormatted, duration, tag).getBytes(), StandardOpenOption.APPEND);
            Thread.sleep(1000L);
        }
    }
}
