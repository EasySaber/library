package com.example.sshomework.config.Serializer;

import com.example.sshomework.entity.LibraryCard;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Aleksey Romodin
 */
public class PersonLibraryCardHistorySerializer extends JsonSerializer<LibraryCard> {

    @Override
    public void serialize(LibraryCard input, JsonGenerator output, SerializerProvider serializerProvider) throws IOException {
        output.writeStartObject();
        output.writeNumberField("id", input.getId());
        output.writeStringField("bookName", input.getBook().getBookName());
        output.writeStringField("dateTimeCreated", transformation(input.getDateTimeCreated()));
        output.writeStringField("dateTimeReturn", transformation(input.getDateTimeReturn()));
        output.writeEndObject();
    }

    private String transformation(ZonedDateTime zonedDateTime) {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss").format(zonedDateTime);
    }
}
