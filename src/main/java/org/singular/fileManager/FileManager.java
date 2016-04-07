package org.singular.fileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public interface FileManager {
    File createFolderIfNotExists(String folder);
    void storeFile(String file, String content) throws FileNotFoundException, UnsupportedEncodingException;
}
