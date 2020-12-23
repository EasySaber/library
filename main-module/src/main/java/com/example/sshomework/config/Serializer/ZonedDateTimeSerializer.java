package com.example.sshomework.config.Serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Aleksey Romodin
 */
public class ZonedDateTimeSerializer extends JsonSerializer<ZonedDateTime> {

    @Override
    public void serialize(ZonedDateTime input, JsonGenerator output, SerializerProvider serializerProvider) throws IOException {
        output.writeString(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss").format(input));
    }
}
