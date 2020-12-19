package com.example.sshomework.repository.book;

import com.example.sshomework.dto.book.BookSearchRequest;
import com.example.sshomework.entity.Book;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
public interface CustomBookRepository {
    List<Book> customFilter(BookSearchRequest request);
}
