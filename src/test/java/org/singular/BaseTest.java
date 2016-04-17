package org.singular;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BaseTest {

    protected String testDir = System.getProperty("user.dir") + "/src/test/resources/";

    protected String getContent(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String content = "";
        String line;
        while ((line = reader.readLine()) != null) {
            content = content + line + "\n";
        }
        reader.close();
        return content;
    }
}
