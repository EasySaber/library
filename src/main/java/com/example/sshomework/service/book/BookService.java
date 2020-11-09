package com.example.sshomework.service.book;

import com.example.sshomework.dto.Book;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
public interface BookService {
    List<Book> getAll();
    List<Book> findByAuthor(String author);
    List<Book> findByAuthorByName(String author, String name);
    void addNewBook(Book book);
    void deleteBook(String author, String name);
}
