package org.singular.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.junit.Test;
import org.singular.BaseTest;
import org.singular.dto.LogLine;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LogParserTest extends BaseTest {

    private LogParser logParser = new LogParser();

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void parseLogTest() throws IOException {
        mapper.registerModule(new JodaModule());
        File log = new File(testDir + "loglines4Test.log");
        String content = getContent(log);

        List<LogLine> logLines = logParser.parseLogs(content);
        String bla = mapper.writeValueAsString(logLines);
        assertEquals(logLines.size(), 8);
    }
}
