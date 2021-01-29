package com.example.sshomework.config.Serializer;

import com.example.sshomework.entity.Book;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author Aleksey Romodin
 */
public class BookOfEntityHistorySerializer extends JsonSerializer<Book> {

    @Override
    public void serialize(Book input, JsonGenerator output, SerializerProvider serializerProvider) throws IOException {
        output.writeStartObject();
        output.writeNumberField("id", input.getId());
        output.writeStringField("bookName", input.getBookName());
        output.writeEndObject();
    }


}
