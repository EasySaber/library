package com.example.sshomework.config.Serializer;

import com.example.sshomework.entity.Genre;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author Aleksey Romodin
 */
public class BookGenreSerializer extends JsonSerializer<Genre> {
    @Override
    public void serialize(Genre input, JsonGenerator output, SerializerProvider serializerProvider) throws IOException {
        output.writeStartObject();
        output.writeNumberField("id", input.getId());
        output.writeStringField("genre", input.getGenreName());
        output.writeEndObject();
    }
}
