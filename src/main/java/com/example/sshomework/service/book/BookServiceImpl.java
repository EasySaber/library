package com.example.sshomework.service.book;

import com.example.sshomework.dto.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return bookList.stream()
                .filter(book -> book.getAuthor()
                .equals(author)).collect(Collectors.toList());
    }

    @Override
    public void addNewBook(Book book) {
        bookList.add(book);
    }

    @Override
    public void deleteBook(String author, String name) {
            bookList.removeIf(searchBook ->
                    searchBook.getAuthor().equals(author) & searchBook.getName().equals(name));
    }

    @Override
    public List<Book> findByAuthorByName(String author, String name) {
        return bookList.stream()
                .filter(book -> book.getAuthor().equals(author) & book.getName().equals(name))
                .collect(Collectors.toList());
    }
}
