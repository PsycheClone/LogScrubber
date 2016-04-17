package org.singular;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.singular.dto.LogLine;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataFactory extends BaseTest {

    private ObjectMapper mapper = new ObjectMapper();

    public DataFactory() {
        this.mapper.registerModule(new JodaModule());
    }

    public List<LogLine> getLoglines() throws IOException {
        File file = new File(testDir + "datafactoryJson.txt");
        return mapper.readValue(getContent(file), new TypeReference<List<LogLine>>() {});
    }
}
