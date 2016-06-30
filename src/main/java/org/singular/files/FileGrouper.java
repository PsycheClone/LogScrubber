package org.singular.files;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileGrouper {

    public static List<List<File>> groupBySlice(List<File> files, int slice) {
        List<List<File>> groupedBySlice = new ArrayList<List<File>>();
        boolean grouping;
        boolean newGroup = true;
        DateTime start = new DateTime();
        DateTime end;
        File file = new File("");

        Iterator<File> iterator = files.iterator();
        while(iterator.hasNext()) {
            grouping = true;
            List<File> groupToAdd = new ArrayList<File>();
            if(newGroup) {
                file = iterator.next();
                newGroup = false;
            }

            start = new DateTime(FileManager.getStartParsable(file.getName()));

            while(grouping) {
                end = new DateTime(FileManager.getEndParsable(file.getName()));
                if(end.isBefore(start.plusMinutes(slice))) {
                    groupToAdd.add(file);
                    if(iterator.hasNext()) {
                        file = iterator.next();
                    } else {
                        newGroup = true;
                        grouping = false;
                    }
                } else if(end.isEqual(start.plusMinutes(slice))) {
                    groupToAdd.add(file);
                    newGroup = true;
                    grouping = false;
                } else {
                    newGroup = false;
                    grouping = false;
                }
            }
            groupedBySlice.add(groupToAdd);
        }
        return groupedBySlice;
    }
}
