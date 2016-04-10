package org.singular;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.singular.entities.Perf4jLog;
import org.singular.entities.Perf4jLogLine;
import org.singular.entities.Range;
import org.singular.parser.DatasetConverter;

import static org.junit.Assert.assertEquals;

public class DatasetConverterTest {

    private Perf4jLog logToConvert;
    private Perf4jLog log2ToConvert;

    private String singleResultWitAverageAndCount;
    private String singleResult;
    private String listResult;

    private DatasetConverter datasetConverter = new DatasetConverter();

    @Before
    public void before() {
        logToConvert = new Perf4jLog();
        Range range = new Range(new DateTime().minusHours(1), new DateTime());
        logToConvert.setRange(range);
        Perf4jLogLine logLine = new Perf4jLogLine();
        logLine.setTag("TestTag");
        logLine.setAverage(23.0);
        logLine.setCount(3);
        logToConvert.addLogLine(logLine);

        log2ToConvert = new Perf4jLog();
        log2ToConvert.setRange(range);
        log2ToConvert.addLogLine(logLine);
        log2ToConvert.addLogLine(logLine);

        singleResultWitAverageAndCount = "{\"range\": \""+range+"\", \"chartData\": {\"cols\": [{\"label\": \"Tag\", \"type\": \"string\"},{\"label\": \"Average\", \"type\": \"number\"},{\"label\": \"Count\", \"type\": \"number\"}], \"rows\": [{\"c\":[{\"v\": \"TestTag\"},{\"v\": 23.0, \"f\": \"0,1 seconds\"},{\"v\": 3, \"f\": \"3 times\"}]}]}}";
        singleResult = "{\"range\": \""+range+"\", \"chartData\": {\"cols\": [{\"label\": \"Tag\", \"type\": \"string\"},{\"label\": \"Average\", \"type\": \"number\"}], \"rows\": [{\"c\":[{\"v\": \"TestTag\"},{\"v\": 23.0, \"f\": \"0,1 seconds in 3 times\"}]}]}}";
        listResult = "{\"range\": \""+range+"\", \"chartData\": {\"cols\": [{\"label\": \"Tag\", \"type\": \"string\"},{\"label\": \"Average\", \"type\": \"number\"},{\"label\": \"Count\", \"type\": \"number\"}], \"rows\": [{\"c\":[{\"v\": \"TestTag\"},{\"v\": 23.0, \"f\": \"0,1 seconds\"},{\"v\": 3, \"f\": \"3 times\"}]},{\"c\":[{\"v\": \"TestTag\"},{\"v\": 23.0, \"f\": \"0,1 seconds\"},{\"v\": 3, \"f\": \"3 times\"}]}]}}";
    }

    @Test
    public void createDatasetWithAverageAndCountTest() {
        String dataset = datasetConverter.convertToBarchartWithAverageAndCountLabels(logToConvert);
        assertEquals(dataset, singleResultWitAverageAndCount);
    }

    @Test
    public void createDatasetTest() {
        String dataset = datasetConverter.convertToBarchart(logToConvert);
        assertEquals(dataset, singleResult);
    }

    @Test
    public void createDatasetFromListTest() {
        String dataset = datasetConverter.convertToBarchartWithAverageAndCountLabels(log2ToConvert);
        assertEquals(dataset, listResult);
    }
}
