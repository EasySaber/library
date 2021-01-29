package com.example.sshomework.config.Serializer;

import com.example.sshomework.entity.LibraryCard;
import com.example.sshomework.entity.Person;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Aleksey Romodin
 */
public class BookLibraryCardHistorySerializer extends JsonSerializer<LibraryCard> {

    @Override
    public void serialize(LibraryCard input, JsonGenerator output, SerializerProvider serializerProvider) throws IOException {
        Person person = input.getPerson();
        String fullName = person.getFirstName() + " " + person.getMiddleName() + " " + person.getLastName();

        output.writeStartObject();
        output.writeNumberField("id", input.getId());
        output.writeStringField("personName", fullName);
        output.writeStringField("dateTimeCreated", transformation(input.getDateTimeCreated()));
        output.writeStringField("dateTimeReturn", transformation(input.getDateTimeReturn()));
        output.writeEndObject();
    }

    private String transformation(ZonedDateTime zonedDateTime) {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss").format(zonedDateTime);
    }
}
