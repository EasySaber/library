package com.example.sshomework.service.book;

import com.example.sshomework.dto.book.BookDto;
import com.example.sshomework.dto.book.BookSearchRequest;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
public interface BookService {
    BookDto addNewBook(BookDto bookDto);
    Boolean deleteBookById(Long id);
    List<BookDto> getByGenre(Long id);
    List<BookDto> getByAuthorFilter(String firstName, String middleName, String lastName);
    BookDto updateGenres(Long id, List<Long> genres);
    List<BookDto> getBookInParameters(BookSearchRequest request);
}
