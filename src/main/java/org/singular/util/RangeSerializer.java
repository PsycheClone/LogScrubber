package org.singular.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.singular.entities.Range;

import java.io.IOException;

public class RangeSerializer extends JsonSerializer<Range> {

    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void serialize(Range value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException, JsonProcessingException {

        gen.writeString(formatter.print(value.getStart()) + " to " + (formatter.print(value.getEnd())));
    }
}