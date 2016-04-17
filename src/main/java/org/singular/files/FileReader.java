package org.singular.files;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class FileReader {

    public String getContent(List<File> files) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String content;

        for(File file : files) {
            BufferedReader reader = new BufferedReader(new java.io.FileReader(file));
            while((content = reader.readLine()) != null) {
                stringBuilder.append(content + "\n");
            }
            reader.close();
        }
        return stringBuilder.toString();
    }

    public String getContent(File file) throws IOException {
        return getContent(Lists.newArrayList(file));
    }
}
