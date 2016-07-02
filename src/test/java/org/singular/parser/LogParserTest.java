package org.singular.parser;

import org.junit.Test;
import org.singular.BaseTest;
import org.singular.dto.LogLine;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LogParserTest extends BaseTest {

    private LogParser logParser = new LogParser();

    @Test
    public void parseLogTest() throws IOException {
        File log = new File(testDir + "logParser/loglines4ReverseTest.log");
        String content = getContent(log);

        List<LogLine> logLines = logParser.parseLogs(content);
        assertEquals(logLines.size(), 8);
    }
}
