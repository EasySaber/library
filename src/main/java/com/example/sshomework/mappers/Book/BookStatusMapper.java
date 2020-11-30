package com.example.sshomework.mappers.Book;

import com.example.sshomework.dto.book.BookStatusDto;
import com.example.sshomework.entity.Author;
import com.example.sshomework.entity.Book;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Mapper
public interface BookStatusMapper {

    @Mapping(target = "authorBook", ignore = true)
    @Mapping(target = "genreList", ignore = true)
    @Mapping(target = "status", ignore = true)
    BookStatusDto toDto(Book book);

    @AfterMapping
    default void setBookAuthor(@MappingTarget BookStatusDto bookStatusDto, Book book) {
        Author author = book.getAuthorBook();

        List<String> genres = new ArrayList<>();
        book.getGenres().forEach(genre -> genres.add(genre.getGenreName()));

        bookStatusDto.setStatus(book.getPersons().isEmpty() ? "Свободна" : "В пользовании");
        bookStatusDto.setGenreList(genres.toString()
                .replace("[","")
                .replace("]", "")
                .trim());
        bookStatusDto.setAuthorBook(author.getFirstName() + " "+ author.getMiddleName() + " " + author.getLastName());
    }

    List<BookStatusDto> toDtoList(List<Book> books);
}
