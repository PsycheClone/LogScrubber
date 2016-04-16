package org.singular.fileManager;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.tools.ant.DirectoryScanner;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class FileManagerImpl implements FileManager {

    private final String rootDir = System.getProperty("user.dir") + "/logs/";

    private DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private Logger LOGGER = LoggerFactory.getLogger(FileManagerImpl.class);

    @Override
    public File createFolderIfNotExists(String folder) {
        createRootFolderIfNotExists();
        File checkFolder = new File(rootDir+folder);
        if (!checkFolder.exists()) {
            checkFolder.mkdir();
            LOGGER.debug(checkFolder + " created.");
        } else {
            LOGGER.debug(checkFolder + " exists.");
        }
        return checkFolder;
    }

    @Override
    public void storeFilesWithIndex(String folderName, String filename, String content) throws FileNotFoundException, UnsupportedEncodingException {
        int fileIndex = 0;

        File folder = createFolderIfNotExists(folderName);

        File file = new File(folder + "/" + filename + fileIndex + ".log");
        while(file.exists()) {
            fileIndex++;
            file = new File(folder + "/" + filename + fileIndex + ".log");
        }
        PrintWriter writer = new PrintWriter(file, "UTF-8");
        writer.write(content);
        writer.close();
    }

    @Override
    public void storeFile(String filename, String content) throws FileNotFoundException, UnsupportedEncodingException {

        File folder = createRootFolderIfNotExists();
        File file = new File(folder + "/" + filename + ".log");

        PrintWriter writer = new PrintWriter(file, "UTF-8");
        writer.write(content);
        writer.close();
    }

    @Override
    public File getFile(String fileName) {
        return null;
    }

    @Override
    public List<String> getAvailableStartTimes(String host) {
        return Lists.transform(getFilteredFiles(host), new Function<String, String>() {
            @Override
            public String apply(String fileName) {
                return getStart(fileName);
            }
        });
    }

    @Override
    public List<String> getAllFiles() {
        File folder = new File(rootDir);
        File[] listOfFiles = folder.listFiles();

        List<String> fileNames = Lists.transform(Arrays.asList(listOfFiles), new Function<File, String>() {
            @Override
            public String apply(File file) {
                return file.getName();
            }
        });

        return fileNames;
    }

    @Override
    public List<String> getFilteredFiles(String host) {
        List<String> filtered = Lists.newArrayList(Collections2.filter(getAllFiles(), Predicates.containsPattern(host)));

        return filtered;
    }

    @Override
    public String getStart(String fileName) {
        return formatToDateTimeString(splitFileName(fileName)[1]);
    }

    @Override
    public String getEnd(String fileName) {
       return formatToDateTimeString(splitFileName(fileName)[2].substring(0, splitFileName(fileName)[2].indexOf(".log")));
    }

    @Override
    public String getStartFormatted(String fileName) {
        return getStart(fileName).replace(" ", "T");
    }

    @Override
    public String getEndFormatted(String fileName) {
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
