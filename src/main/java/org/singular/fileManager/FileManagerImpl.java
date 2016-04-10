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
    public void storeFile(String folderName, String filename, String content) throws FileNotFoundException, UnsupportedEncodingException {

        File folder = createFolderIfNotExists(folderName);
        File file = new File(folder + "/" + filename + ".log");

        PrintWriter writer = new PrintWriter(file, "UTF-8");
        writer.write(content);
        writer.close();
    }

    @Override
    public File getFile(String fileName) {
        return null;
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
