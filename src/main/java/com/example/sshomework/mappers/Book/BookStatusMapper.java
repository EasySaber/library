package com.example.sshomework.mappers.Book;

import com.example.sshomework.dto.book.BookStatusDto;
import com.example.sshomework.entity.Book;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

        List<String> genres = new ArrayList<>();

        Optional.of(book.getAuthorBook()).ifPresent(author ->
                bookStatusDto.setAuthorBook(author.getFirstName() + " "+ author.getMiddleName() + " " + author.getLastName()));

        Optional.ofNullable(book.getGenres()).ifPresent(gs -> {
            if (!gs.isEmpty()) {
                gs.forEach(genre -> genres.add(genre.getGenreName()));
            }
        });
        bookStatusDto.setGenreList(genres.isEmpty() ? "Без жанра" : genres.toString().replaceAll("^\\[|]$","").trim());


        bookStatusDto.setStatus(
                Optional.ofNullable(book.getPersons()).map(Set::isEmpty)
                        .equals(Optional.of(false)) ? "В пользовании" : "Свободна"
        );
    }

    List<BookStatusDto> toDtoList(List<Book> books);
}
