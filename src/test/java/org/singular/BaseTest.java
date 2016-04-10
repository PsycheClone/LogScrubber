package org.singular;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.singular.entities.Perf4jLog;
import org.singular.parser.DatasetConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BaseTest {

    protected String rootDir = System.getProperty("user.dir") + "/src/test/resources/";
    protected File logExample = new File(rootDir + "logExample.txt");

    private ObjectMapper objectMapper;
    private DatasetConverter datasetConverter;

    protected String getContent(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String content = "";
        String line;
        while ((line = reader.readLine()) != null) {
            content = content + line;
        }
        Perf4jLog log = objectMapper.readValue(content, Perf4jLog.class);
        return datasetConverter.convertToBarchart(log);
    }
}
