package org.singular.creator;

import org.singular.dto.TagDataset;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TagChartCreator extends AbstractCreator<TagDataset> {

    @Override
    protected List<File> getLogs(String host, String from, int range) throws IOException {
        return null;
    }

    @Override
    protected List<TagDataset> calculate(List<File> files) throws IOException {
        return null;
    }
}
