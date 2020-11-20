package com.example.sshomework.mappers;

import com.example.sshomework.dto.BookDto;
import com.example.sshomework.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Aleksey Romodin
 */
@Mapper
public interface BookMapper extends MapMapper<BookDto, Book> {
    @Mapping(source = "authorBookDto.id", target = "authorBook.id")
    Book toEntity(BookDto book);
}
