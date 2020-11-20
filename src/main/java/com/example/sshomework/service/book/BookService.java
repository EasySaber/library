package com.example.sshomework.service.book;

import com.example.sshomework.dto.BookDto;
import com.example.sshomework.entity.Book;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
public interface BookService {
    Book addNewBook(BookDto bookDto);
    Boolean deleteBookById(Long id);
    List<Book> getByGenre(Long id);
    List<Book> getByAuthorFilter(String firstName, String middleName, String lastName);
    Book updateGenres(Long id, List<Long> genres);
}
