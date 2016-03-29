package org.singular.fileManager;

import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileFilter;

public class FileNameFilter<T> implements GenericFileFilter<T> {
    public boolean accept(GenericFile<T> file) {
        // we want all directories
        if (file.isDirectory()) {
            return true;
        }
        // we dont accept any files starting with skip in the name
        return !file.getFileName().contains("-done");
    }
}