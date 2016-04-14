package org.singular.fileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface FileManager {
    File createFolderIfNotExists(String folder);
    void storeFilesWithIndex(String folder, String file, String content) throws FileNotFoundException, UnsupportedEncodingException;
    void storeFile(String file, String content) throws FileNotFoundException, UnsupportedEncodingException;
    File getFile(String fileName);
    List<String> getAvailableRanges(String host);
}
