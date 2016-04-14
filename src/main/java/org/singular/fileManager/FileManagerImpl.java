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

    private DirectoryScanner directoryScanner = new DirectoryScanner();

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
    public List<String> getAvailableRanges(String host) {
        File folder = new File(rootDir);
        File[] listOfFiles = folder.listFiles();

        List<String> timeslices = Lists.transform(Arrays.asList(listOfFiles), new Function<File, String>() {
            @Override
            public String apply(File file) {
                return file.getName();
            }
        });

        List<String> filtered = Lists.newArrayList(Collections2.filter(timeslices, Predicates.containsPattern(host)));

        return Lists.transform(filtered, new Function<String, String>() {
            @Override
            public String apply(String fileName) {
                return getRangeFromFileName(fileName);
            }
        });
    }

    private String getRangeFromFileName(String fileName) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String[] parts = fileName.split("_");
        String from = parts[1];
        String till = parts[2].substring(0, parts[2].indexOf(".log"));
        return fmt.print(new DateTime(Long.valueOf(from))) + " to " + fmt.print(new DateTime(Long.valueOf(till)));
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
