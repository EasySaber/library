package com.example.sshomework.service.book;

import com.example.sshomework.dto.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Service
public class BookServiceImpl implements BookService {

    private static final List<Book> bookList = new ArrayList<>();

    static {
        bookList.add(new Book("Мастер и Маргарита", "Михаил Булгаков", "роман"));
        bookList.add(new Book("Анна Каренина", "Лев Толстой", "роман"));
        bookList.add(new Book("Евгений Онегин", "Александр Пушкин", "роман"));
    }

    @Override
    public List<Book> getAll() {
        return bookList;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        List<Book> findBooks = new ArrayList<>();
        for (Book book : bookList) {
            if (book.author.equals(author)) {
                findBooks.add(book);
            }
        }
        return findBooks;
    }

    @Override
    public void addNewBook(Book book) {
        bookList.add(book);
    }

    @Override
    public void deleteBook(String author, String name) {
        if (!bookList.isEmpty()) {
            bookList.removeIf(searchBook ->
                    searchBook.author.equals(author) & searchBook.name.equals(name));
        }
    }

    @Override
    public List<Book> findByAuthorByName(String author, String name) {
        List<Book> findBooks = new ArrayList<>();
        for (Book book : bookList) {
            if (book.author.equals(author) & book.name.equals(name)) {
                findBooks.add(book);
            }
        }
        return findBooks;
    }
}
