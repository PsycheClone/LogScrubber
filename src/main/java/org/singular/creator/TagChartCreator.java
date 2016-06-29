package org.singular.creator;

import javafx.util.Pair;
import org.joda.time.DateTime;
import org.singular.dto.Data;
import org.singular.dto.TagDataset;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TagChartCreator extends AbstractCreator<TagDataset> {

    @Override
    protected Data createDataset(Map.Entry<String, List<Pair<DateTime, Long>>> metrics) {
        return null;
    }

    @Override
    protected List<File> getLogs(String host, String from, int range) throws IOException {
        return null;
    }

    @Override
    protected List<TagDataset> calculate(List<List<File>> files) throws IOException {
        return null;
    }
}
