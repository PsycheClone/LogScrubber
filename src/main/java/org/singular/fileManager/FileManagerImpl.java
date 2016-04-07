package org.singular.fileManager;

import org.apache.tools.ant.DirectoryScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

@Component
public class FileManagerImpl implements FileManager {

    private final String rootDir = System.getProperty("user.dir") + "/logs/";

    private DirectoryScanner directoryScanner = new DirectoryScanner();

    private Logger LOGGER = LoggerFactory.getLogger(FileManagerImpl.class);

    @Override
    public File createFolderIfNotExists(String folder) {
        createRootFolderIfNotExists();
        File checkFolder = new File(folder);
        if (!checkFolder.exists()) {
            checkFolder.mkdir();
            LOGGER.debug(checkFolder + " created.");
        } else {
            LOGGER.debug(checkFolder + " exists.");
        }
        return checkFolder;
    }

    @Override
    public void storeFile(String filename, String content) throws FileNotFoundException, UnsupportedEncodingException {
        File jsonFolder = createFolderIfNotExists(rootDir + "json");
        PrintWriter writer = new PrintWriter(jsonFolder + "/" + filename, "UTF-8");
        writer.write(content);
        writer.close();
    }

    private void createRootFolderIfNotExists() {
        if(!new File(rootDir).exists()) {
            File checkFolder = new File(rootDir);
            if (!checkFolder.exists()) {
                checkFolder.mkdir();
                LOGGER.info(checkFolder + " created.");
            } else {
                LOGGER.info(checkFolder + " exists.");
            }
        }
    }
}
