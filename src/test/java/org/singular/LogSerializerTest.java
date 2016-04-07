package org.singular;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.singular.config.ScrubberConfiguration;
import org.singular.entities.Log;
import org.singular.parser.DatasetConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ScrubberConfiguration.class)
public class LogSerializerTest {

    @Autowired
    private ObjectMapper objectMapper;

    private DatasetConverter datasetConverter = new DatasetConverter();

    String rootDir = System.getProperty("user.dir") + "/src/test/resources/";

    @Test
    public void testDeserialize() throws IOException {
        File jsonFile = new File(rootDir + "jsonLog.log");
        String json = getContent(jsonFile);
        Log log = objectMapper.readValue(json, Log.class);
        assertTrue(log.getRange() != null);
    }

    private String getContent(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String content = "";
        String line;
        while ((line = reader.readLine()) != null) {
            content = content + line;
        }
        Log log = objectMapper.readValue(content, Log.class);
        return datasetConverter.convertToBarchart(log);
    }
}
