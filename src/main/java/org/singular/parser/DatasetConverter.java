package org.singular.parser;

import org.singular.entities.Log;
import org.singular.entities.LogLine;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DatasetConverter {

    private String preString = "{\"cols\": [{\"label\": \"Tag\", \"type\": \"string\"}";
    private String average = ",{\"label\": \"Average\", \"type\": \"number\"}";
    private String count = ",{\"label\": \"Count\", \"type\": \"number\"}";
    private String rows = "], \"rows\": [";
    private String postString = "]}}";

    public String convertToBarchartWithAverageAndCountLabels(Log log) {
        StringBuilder stringBuilder = new StringBuilder();
        prependRange(stringBuilder, log);
        averageAndCountLabels(stringBuilder);
        columnsForAverageAndCount(stringBuilder, log);
        postString(stringBuilder);
        return stringBuilder.toString();
    }

    private void prependRange(StringBuilder stringBuilder, Log log) {
        stringBuilder.append("{\"range\": \""+ log.getRange() + "\", \"chartData\": ");
    }


    public String convertToBarchart(Log log) {
        StringBuilder stringBuilder = new StringBuilder();
        prependRange(stringBuilder, log);
        averageLabel(stringBuilder);
        columnsForAverage(stringBuilder, log);
        postString(stringBuilder);
        return stringBuilder.toString();
    }

    private void averageAndCountLabels(StringBuilder stringBuilder) {
        stringBuilder.append(preString + average + count + rows);
    }

    private void averageLabel(StringBuilder stringBuilder) {
        stringBuilder.append(preString + average + rows);
    }

    private void columnsForAverage(StringBuilder stringBuilder, Log log) {
        int amountOfLogs = log.getLogLines().size();
        int index = 0;
        for(LogLine logLine : log.getLogLines()) {
            index ++;
            stringBuilder.append("{\"c\":[");
            columnValue(stringBuilder, "\""+logLine.getTag()+"\"");
            stringBuilder.append(",");
            columnValue(stringBuilder, String.valueOf(logLine.getAverage()), setTooltip(logLine));
            stringBuilder.append("]}");
            if(index != amountOfLogs) {
                stringBuilder.append(",");
            }
        }
    }

    private void columnsForAverageAndCount(StringBuilder stringBuilder, Log log) {
        int amountOfLogs = log.getLogLines().size();
        int index = 0;
        for(LogLine logLine : log.getLogLines()) {
            index ++;
            stringBuilder.append("{\"c\":[");
            columnValue(stringBuilder, "\""+logLine.getTag()+"\"");
            stringBuilder.append(",");
            columnValue(stringBuilder, String.valueOf(logLine.getAverage()), setTooltipForAverageAndCount(logLine));
            stringBuilder.append(",");
            columnValue(stringBuilder, String.valueOf(logLine.getCount()), logLine.getCount() + " times");
            stringBuilder.append("]}");
            if(index != amountOfLogs) {
                stringBuilder.append(",");
            }
        }
    }

    private void columnValue(StringBuilder stringBuilder, String value, String display) {
        stringBuilder.append("{\"v\": ");
        stringBuilder.append(value);
        if(display != null) {
            stringBuilder.append(", \"f\": ");
            stringBuilder.append("\""+display+"\"");
        }
        stringBuilder.append("}");
    }

    private void postString(StringBuilder stringBuilder) {
        stringBuilder.append(postString);
    }

    private String setTooltip(LogLine logLine) {
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(logLine.getAverage() / 1000) + " seconds in " + logLine.getCount() + " times";
    }

    private String setTooltipForAverageAndCount(LogLine logLine) {
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(logLine.getAverage() / 1000) + " seconds";
    }

    private void columnValue(StringBuilder stringBuilder, String value) {
        columnValue(stringBuilder, value, null);
    }
}