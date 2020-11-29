package com.example.sshomework.mappers;

import com.example.sshomework.dto.book.BookDto;
import com.example.sshomework.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Mapper(componentModel = "spring")
public interface BookMapper extends MapMapper<BookDto, Book> {
    @Mapping(source = "authorBookDto", target = "authorBook")
    Book toEntity(BookDto book);


    @Mapping(target = "authorBookDto", source = "authorBook")
    @Mapping(target = "authorBookDto.books", ignore = true)
    @Mapping(target = "personsDto", ignore = true)
    BookDto toDto(Book book);

    @Mapping(source = "authorBook", target = "authorBookDto")
    List<BookDto> toDtoList(List<Book> books);

}

