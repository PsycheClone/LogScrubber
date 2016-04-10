package org.singular;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.singular.config.ScrubberConfiguration;
import org.singular.entities.Perf4jLog;
import org.singular.parser.DatasetConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ScrubberConfiguration.class)
public class LogSerializerTest extends BaseTest {

    @Autowired
    private ObjectMapper objectMapper;

    private DatasetConverter datasetConverter = new DatasetConverter();

    String rootDir = System.getProperty("user.dir") + "/src/test/resources/";

    @Test
    public void testDeserialize() throws IOException {
        File jsonFile = new File(rootDir + "jsonLog.log");
        String json = getContent(jsonFile);
        Perf4jLog log = objectMapper.readValue(json, Perf4jLog.class);
        assertTrue(log.getRange() != null);
    }
}
