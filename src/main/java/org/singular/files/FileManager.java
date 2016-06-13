package org.singular.files;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class FileManager {

    @Value("${deleteOnStartup}")
    private boolean deleteOnStartup;

    @Value("${timeslice}")
    private int timeslice;

    private String rootDir = System.getProperty("user.home") + "/logs/";

    private DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private Logger LOGGER = LoggerFactory.getLogger(FileManager.class);

    @PostConstruct
    public void deleteAllLogFiles() throws IOException {
        if(deleteOnStartup) {
            LOGGER.info("Clearing logs folder...");
            createRootFolderIfNotExists();
            FileUtils.cleanDirectory(new File(rootDir));
        } else {
            if(!isLogsFolderConsistent()) {
                LOGGER.error(System.getProperty("user.home") + "/logs folder contains timeslices greater of smaller than requested timeslice.  Either set scrub=false or set deleteOnStartup=true");
                System.exit(1);
            }
        }
    }

    private boolean isLogsFolderConsistent() {
        boolean consistent = true;
        for(File file : getAllFiles()) {
            long result = new DateTime(getEndParsable(file.getName())).getMillis() - new DateTime(getStartParsable(file.getName())).getMillis();
            if(result != timeslice * 60 * 1000) {
                LOGGER.info(file.getName() + " not consistent!");
                consistent = false;
            }
        }
        return consistent;
    }

    public void storeFile(String filename, String content) throws FileNotFoundException, UnsupportedEncodingException {
        File folder = createRootFolderIfNotExists();
        File file = new File(folder + "/" + filename);

        PrintWriter writer = new PrintWriter(file, "UTF-8");
        writer.write(content);
        writer.close();
    }

    public List<String> getAvailableStartTimes(String host) {
        List<String> fileNames = Lists.transform((getFilteredFiles(host)), new Function<File, String>() {
            @Override
            public String apply(File file) {
                return file.getName();
            }
        });

        return Lists.transform(fileNames, new Function<String, String>() {
            
            public String apply(String fileName) {
                return getStart(fileName);
            }
        });
    }

    public Set<String> getAllHosts() {
        List<File> uniqueHosts = getAllFiles();
        Set<String> availableHosts = Sets.newTreeSet(Lists.transform(uniqueHosts, new Function<File, String>() {
            @Override
            public String apply(File file) {
                return getHostName(file.getName());
            }
        }));
        return availableHosts;
    }

    public List<File> getAllFiles() {
        File folder = new File(rootDir);
        File[] listOfFiles = folder.listFiles();

        return Arrays.asList(listOfFiles);
    }

    public List<File> getFilteredFilesWithinRange(final String host, final String from, int range) {
        final DateTime fromTime = new DateTime(from);
        final DateTime tillTime = fromTime.plusMinutes(range);
        List<File> filtered = getFiltered(host, fromTime, tillTime);
        Collections.sort(filtered);
        return filtered;
    }

    public List<File> getFilteredFilesBeforeAndAfter(final String host, final String from, int range) {
        final DateTime time = new DateTime(from);
        final DateTime fromTime = time.minusMinutes(range);
        final DateTime tillTime = time.plusMinutes(range);
        List<File> filtered = getFiltered(host, fromTime, tillTime);
        Collections.sort(filtered);
        return filtered;
    }

    private List<File> getFiltered(final String host, final DateTime fromTime, final DateTime tillTime) {
        return Lists.newArrayList(Collections2.filter(getAllFiles(), new Predicate<File>() {
            @Override
            public boolean apply(File file) {
                String fileName = file.getName();
                DateTime start = new DateTime(getStartParsable(fileName));
                DateTime end = new DateTime(getEndParsable(fileName));
                if(fileName.contains(host)) {
                    if(start.isAfter(fromTime) || start.isEqual(fromTime)) {
                        if(end.isBefore(tillTime) || end.isEqual(tillTime)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }));
    }

    public List<File> getFilteredFiles(final String host) {
        List<File> filtered = Lists.newArrayList(Collections2.filter(getAllFiles(), new Predicate<File>() {
            @Override
            public boolean apply(File file) {
                return file.getName().contains(host);
            }
        }));

        return filtered;
    }

    public String getHostName(String fileName) {
        return splitFileName(fileName)[0];
    }

    public String getStart(String fileName) {
        return formatToDateTimeString(splitFileName(fileName)[1]);
    }

    public String getEnd(String fileName) {
       return formatToDateTimeString(splitFileName(fileName)[2].substring(0, splitFileName(fileName)[2].indexOf(".log")));
    }

    public String getStartParsable(String fileName) {
        return getStart(fileName).replace(" ", "T");
    }

    public String getEndParsable(String fileName) {
        return getEnd(fileName).replace(" ", "T");
    }

    private String[] splitFileName(String fileName) {
        return fileName.split("_");
    }

    private String formatToDateTimeString(String millis) {
        return fmt.print(new DateTime(Long.valueOf(millis)));
    }

    private File createRootFolderIfNotExists() {
        File checkFolder = new File(rootDir);
        if (!checkFolder.exists()) {
            checkFolder.mkdir();
            LOGGER.debug(checkFolder + " created.");
        } else {
            LOGGER.debug(checkFolder + " exists.");
        }
        return checkFolder;
    }
}
