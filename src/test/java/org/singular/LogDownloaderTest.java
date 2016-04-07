package org.singular;

import com.jcraft.jsch.JSchException;
import org.junit.Test;
import org.singular.connect.Retriever;

import java.io.IOException;

public class LogDownloaderTest {
    private final static String host = "esb-a-test.erfurt.elex.be";
    private final static String esbPath = "/usr/share/apache-servicemix/data/log/perf4j.log";
    private Retriever logDownloader = new Retriever();

    @Test
    public void test() throws IOException, JSchException {
        logDownloader.retrieveLogs(host, esbPath);
    }
}
