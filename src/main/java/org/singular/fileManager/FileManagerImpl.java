package org.singular.fileManager;

import org.apache.tools.ant.DirectoryScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

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
            LOGGER.info(checkFolder + " created.");
        } else {
            LOGGER.info(checkFolder + " exists.");
        }
        return checkFolder;
    }

    @Override
    public boolean folderExists(String folder) {
        return new File(folder).exists();
    }

    @Override
    public String[] scanFolder(String folder, String wildcard) {
        directoryScanner.setIncludes(new String[]{wildcard});
        directoryScanner.setBasedir(folder);
        directoryScanner.setCaseSensitive(false);
        directoryScanner.scan();
        return directoryScanner.getIncludedFiles();
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
