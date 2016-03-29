package org.singular;

import org.singular.connect.LogDownloader;
import org.singular.parser.Perf4jParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;

@SpringBootApplication
@ComponentScan
public class Scrubber {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Scrubber.class, args);

//        Perf4jParser perf4jParser = new Perf4jParser();
//        String file = System.getProperty("user.dir");
//        File file2 = new File(file+"/logs/esb-a-test.erfurt.elex.be.log");
//        System.out.println(file2.getAbsolutePath());
//        perf4jParser.parse(file2.getAbsolutePath(), "esb-a-test.erfurt.elex.be", 180000);
    }
}
