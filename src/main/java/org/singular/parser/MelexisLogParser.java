package org.singular.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MelexisLogParser {
    private final Pattern timestampPattern = Pattern.compile("\\d+-\\d+-\\d+\\s\\d+:\\d+:\\d+");

    public String getTimestamp(String line) {
        Matcher matcher = timestampPattern.matcher(line);
        if(matcher.find()) {
            return matcher.group().replace(" ", "T");
        }
        return null;
    }
}
