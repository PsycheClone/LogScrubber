package org.singular.fileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public interface FileManager {
    File createFolderIfNotExists(String folder);
    void storeFilesWithIndex(String folder, String file, String content) throws FileNotFoundException, UnsupportedEncodingException;
    void storeFile(String folder, String file, String content) throws FileNotFoundException, UnsupportedEncodingException;
    File getFile(String fileName);
}
