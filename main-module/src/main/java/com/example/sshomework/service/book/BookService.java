package com.example.sshomework.service.book;

import com.example.sshomework.dto.book.BookDto;
import com.example.sshomework.dto.FullNameDto;
import com.example.sshomework.dto.book.BookSearchRequest;
import com.example.sshomework.dto.book.BookStatusDto;
import com.example.sshomework.exception.DeleteRelatedDataException;

import java.util.List;
import java.util.Optional;

/**
 * @author Aleksey Romodin
 */
public interface BookService {
    Optional<BookDto> addNewBook(BookDto bookDto);
    void deleteBookById(Long id) throws DeleteRelatedDataException;
    List<BookDto> getByGenre(String genreName);
    List<BookDto> getByAuthorFilter(FullNameDto request);
    Optional<BookDto> updateGenres(Long id, List<String> genres);
    List<BookDto> getBookInParameters(BookSearchRequest request);
    List<BookStatusDto> getBooks();
}
