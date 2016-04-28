package org.singular.connect;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalConnector {

    public BufferedReader readFileBackwards(String file) throws IOException {
        return new BufferedReader(new StringReader(reverseInput(file)));
    }

    private String reverseInput(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        List<String> stringList = new ArrayList<String>();

        String line;
        while((line = reader.readLine()) != null) {
            stringList.add(line + "\n");
        }

        Collections.reverse(stringList);
        return stringList.toString();
    }
}
