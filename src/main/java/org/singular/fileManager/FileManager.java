package org.singular.fileManager;

import java.io.File;

public interface FileManager {
    File createFolderIfNotExists(String folder);
    boolean folderExists(String folder);
    String[] scanFolder(String folder, String wildcard);
}
