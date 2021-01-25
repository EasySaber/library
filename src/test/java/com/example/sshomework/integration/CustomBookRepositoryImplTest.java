package com.example.sshomework.integration;

import com.example.sshomework.dto.book.BookSearchRequest;
import com.example.sshomework.dto.book.Sings;
import com.example.sshomework.entity.Author;
import com.example.sshomework.entity.Book;
import com.example.sshomework.entity.Genre;
import com.example.sshomework.repository.GenreRepository;
import com.example.sshomework.repository.author.AuthorRepository;
import com.example.sshomework.repository.book.BookRepository;
import com.example.sshomework.repository.book.CustomBookRepositoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Aleksey Romodin
 */
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class CustomBookRepositoryImplTest {
    @Autowired
    private CustomBookRepositoryImpl customBookRepository;

    private static Book bookFirst;
    private static Book bookSecond;
    private static Book bookThird;

    @BeforeAll
    static void setUp(@Autowired BookRepository bookRepository,
                      @Autowired AuthorRepository authorRepository,
                      @Autowired GenreRepository genreRepository) {

        //Автор
        Author authorFirst = new Author(1L, "Петр", "Петрович", "Петров", null, LocalDate.parse("1799-06-06"));
        authorRepository.save(authorFirst);
        //Жанры
        Genre genreFirst = new Genre(1L, "Роман", null);
        Genre genreSecond = new Genre(2L, "Рассказ", null);
        Genre genreThird = new Genre(3L, "Детектив", null);
        genreRepository.save(genreFirst);
        genreRepository.save(genreSecond);
        genreRepository.save(genreThird);
        //Книги
        bookFirst = new Book(1L, "Первая книга", LocalDate.parse("1950-02-01"), authorFirst, new HashSet<>(), null);
        bookSecond = new Book(2L, "Вторая книга", LocalDate.parse("1900-02-01"), authorFirst, new HashSet<>(), null);
        bookThird = new Book(3L, "Третья книга", LocalDate.parse("1880-02-01"), authorFirst, new HashSet<>(), null);

        //Добавление жанров книгам
        bookFirst.getGenres().add(genreFirst);
        bookSecond.getGenres().add(genreSecond);
        bookSecond.getGenres().add(genreThird);
        bookThird.getGenres().add(genreThird);

        bookRepository.save(bookFirst);
        bookRepository.save(bookSecond);
        bookRepository.save(bookThird);
    }

    @Test
    @DisplayName("Пустые параметры фильтра")
    void customFilterAllBooksTest() {
        BookSearchRequest bookSearchRequest = new BookSearchRequest(null, null, null);
        List<Book> books = assertDoesNotThrow(() -> customBookRepository.customFilter(bookSearchRequest));
        assertAll(
                () -> assertEquals(books.size(), 3),
                () -> assertTrue(books.contains(bookFirst)),
                () -> assertTrue(books.contains(bookSecond)),
                () -> assertTrue(books.contains(bookThird))
        );
    }

    @Test
    @DisplayName("Фильтр по жанру")
    void customFilterGenreTest() {
        BookSearchRequest bookSearchRequest = new BookSearchRequest("Роман", null, null);
        List<Book> books = customBookRepository.customFilter(bookSearchRequest);
        assertAll(
                () -> assertEquals(books.size(), 1),
                () -> assertTrue(books.contains(bookFirst))
        );
    }

    @Test
    @DisplayName("Фильтр по жанру + год публикации, без признака")
    void customFilterGenreAndYearTest() {
        BookSearchRequest bookSearchRequest = new BookSearchRequest("Роман", 1900, null);
        List<Book> books = customBookRepository.customFilter(bookSearchRequest);
        assertAll(
                () -> assertEquals(books.size(), 1),
                () -> assertTrue(books.contains(bookFirst))
        );
    }

    @Test
    @DisplayName("Фильтр по жанру + год публикации, =")
    void customFilterGenreAndYearAndEquallyTest() {
        BookSearchRequest bookSearchRequest = new BookSearchRequest("Детектив", 1900, Sings.EQUALLY);
        List<Book> books = customBookRepository.customFilter(bookSearchRequest);
        assertAll(
                () -> assertEquals(books.size(), 1),
                () -> assertTrue(books.contains(bookSecond))
        );
    }

    @Test
    @DisplayName("Фильтр по жанру + год публикации, >")
    void customFilterGenreAndYearAndMoreTest() {
        BookSearchRequest bookSearchRequest = new BookSearchRequest("Детектив", 1850, Sings.MORE);
        List<Book> books = customBookRepository.customFilter(bookSearchRequest);
        assertAll(
                () -> assertEquals(books.size(), 2),
                () -> assertTrue(books.contains(bookThird)),
                () -> assertTrue(books.contains(bookSecond))
        );
    }

    @Test
    @DisplayName("Фильтр по жанру + год публикации, <")
    void customFilterGenreAndYearAndLessTest() {
        BookSearchRequest bookSearchRequest = new BookSearchRequest("Детектив", 1960, Sings.LESS);
        List<Book> books = customBookRepository.customFilter(bookSearchRequest);
        assertAll(
                () -> assertEquals(books.size(), 2),
                () -> assertTrue(books.contains(bookSecond)),
                () -> assertTrue(books.contains(bookThird))
        );
    }

    @Test
    @DisplayName("Фильтр по году публикации, <")
    void customFilterYearAndLessTest() {
        BookSearchRequest bookSearchRequest = new BookSearchRequest(null, 1955, Sings.LESS);
        List<Book> books = customBookRepository.customFilter(bookSearchRequest);
        assertAll(
                () -> assertEquals(books.size(), 3),
                () -> assertTrue(books.contains(bookFirst)),
                () -> assertTrue(books.contains(bookSecond)),
                () -> assertTrue(books.contains(bookThird))
        );
    }

    @Test
    @DisplayName("Фильтр с несуществующими параметрами")
    void customFilterNonexistentTest() {
        BookSearchRequest bookSearchRequest = new BookSearchRequest("Сказка", 2000, Sings.MORE);
        List<Book> books = customBookRepository.customFilter(bookSearchRequest);
        assertTrue(books.isEmpty());
    }

}

