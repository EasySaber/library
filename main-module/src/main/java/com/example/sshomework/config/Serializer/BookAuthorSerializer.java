package com.example.sshomework.config.Serializer;

import com.example.sshomework.entity.Author;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * @author Aleksey Romodin
 */
public class BookAuthorSerializer extends JsonSerializer<Author> {
    @Override
    public void serialize(Author input, JsonGenerator output, SerializerProvider serializerProvider) throws IOException {
        String fullName = input.getFirstName() + " " + input.getMiddleName() + " " + input.getLastName();
        output.writeStartObject();
        output.writeNumberField("id", input.getId());
        output.writeStringField("personName", fullName);
        output.writeStringField("dateOfBirth", DateTimeFormatter.ofPattern("dd-MM-yyyy").format(input.getDateTimeOfBirth()));
        output.writeEndObject();
    }
}
