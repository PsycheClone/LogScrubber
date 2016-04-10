package org.singular;

import com.jcraft.jsch.JSchException;
import org.junit.Test;
import org.singular.scrubber.Scrubber;

import java.io.IOException;

public class LogDownloaderTest {
    private final static String host = "esb-a-test.erfurt.elex.be";
    private final static String esbPath = "/usr/share/apache-servicemix/data/log/perf4j.log";
    private Scrubber retriever = new Scrubber();

    @Test
    public void test() throws IOException, JSchException {
        retriever.retrieveLogs(host, esbPath);
    }
}
