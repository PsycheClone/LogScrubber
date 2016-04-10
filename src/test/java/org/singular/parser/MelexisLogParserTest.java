package org.singular.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.singular.BaseTest;
import org.singular.entities.Perf4jLog;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class MelexisLogParserTest extends BaseTest {

    @Autowired
    private ObjectMapper objectMapper;

    private DatasetConverter datasetConverter = new DatasetConverter();

    @Test
    public void testDeserialize() throws IOException {
        File jsonFile = new File(rootDir + "rawLogs.log");
        String json = getContent(jsonFile);
        Perf4jLog log = objectMapper.readValue(json, Perf4jLog.class);
        assertTrue(log.getRange() != null);
    }


}
