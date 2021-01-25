package com.example.sshomework.integration;

import com.example.sshomework.dto.author.AuthorSearchRequest;
import com.example.sshomework.entity.Author;
import com.example.sshomework.repository.author.AuthorRepository;
import com.example.sshomework.repository.author.CustomAuthorRepositoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Aleksey Romodin
 */
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class CustomAuthorRepositoryImplTest {
    @Autowired
    private CustomAuthorRepositoryImpl customAuthorRepository;

    private static Author authorFirst;
    private static Author authorSecond;
    private static Author authorThird;

    @BeforeAll
    static void setUp(@Autowired AuthorRepository authorRepository) {
        //Автор
        authorFirst = new Author(1L, "Петр", "Петрович", "Петров", null, LocalDate.parse("1799-06-06"));
        authorSecond = new Author(2L, "Петр", "Иванович", "Иванов", null, LocalDate.parse("1799-06-06"));
        authorThird = new Author(3L, "Николай", "Анатольевич", "Петров", null, LocalDate.parse("1799-06-06"));
        authorRepository.save(authorFirst);
        authorRepository.save(authorSecond);
        authorRepository.save(authorThird);
    }

    @Test
    @DisplayName("Пустые параметры фильтра")
    void customFilterNullTest() {
        AuthorSearchRequest request = new AuthorSearchRequest(null, null, null, null, null);
        List<Author> authors = customAuthorRepository.customFilter(request);
        assertAll(
                () -> assertEquals(authors.size(), 3),
                () -> assertTrue(authors.contains(authorFirst)),
                () -> assertTrue(authors.contains(authorSecond)),
                () -> assertTrue(authors.contains(authorThird))
        );
    }

    @Test
    @DisplayName("Фильтр по И")
    void customFilterFirstNameTest() {
        AuthorSearchRequest request = new AuthorSearchRequest("Петр", null, null, null, null);
        List<Author> authors = customAuthorRepository.customFilter(request);
        assertAll(
                () -> assertEquals(authors.size(), 2),
                () -> assertTrue(authors.contains(authorFirst)),
                () -> assertTrue(authors.contains(authorSecond))
        );
    }

    @Test
    @DisplayName("Фильтр по ИО")
    void customFilterFirstNameAndMiddleNameTest() {
        AuthorSearchRequest request = new AuthorSearchRequest("Петр", "Иванович", null, null, null);
        List<Author> authors = customAuthorRepository.customFilter(request);
        assertAll(
                () -> assertEquals(authors.size(), 1),
                () -> assertTrue(authors.contains(authorSecond))
        );
    }

    @Test
    @DisplayName("Фильтр по Ф")
    void customFilterLastNameTest() {
        AuthorSearchRequest request = new AuthorSearchRequest(null, null, "Петров", null, null);
        List<Author> authors = customAuthorRepository.customFilter(request);
        assertAll(
                () -> assertEquals(authors.size(), 2),
                () -> assertTrue(authors.contains(authorFirst)),
                () -> assertTrue(authors.contains(authorThird))
        );
    }

    @Test
    @DisplayName("Фильтр по ФИО")
    void customFilterFullNameTest() {
        AuthorSearchRequest request = new AuthorSearchRequest("Николай", "Анатольевич", "Петров", null, null);
        List<Author> authors = customAuthorRepository.customFilter(request);
        assertAll(
                () -> assertEquals(authors.size(), 1),
                () -> assertTrue(authors.contains(authorThird))
        );
    }

    @Test
    @DisplayName("Фильтр по начальной дате")
    void customFilterStartDateTest() {
        AuthorSearchRequest request = new AuthorSearchRequest(null, null, null, LocalDate.now().minusDays(1), null);
        List<Author> authors = customAuthorRepository.customFilter(request);
        assertAll(
                () -> assertEquals(authors.size(), 3),
                () -> assertTrue(authors.contains(authorSecond)),
                () -> assertTrue(authors.contains(authorSecond)),
                () -> assertTrue(authors.contains(authorThird))
        );
    }

    @Test
    @DisplayName("Фильтр по конечной дате")
    void customFilterEndDateTest() {
        AuthorSearchRequest request = new AuthorSearchRequest(null, null, null, null, LocalDate.now().plusDays(1));
        List<Author> authors = customAuthorRepository.customFilter(request);
        assertAll(
                () -> assertEquals(authors.size(), 3),
                () -> assertTrue(authors.contains(authorSecond)),
                () -> assertTrue(authors.contains(authorSecond)),
                () -> assertTrue(authors.contains(authorThird))
        );
    }

    @Test
    @DisplayName("Фильтр между датами")
    void customFilterBetweenDateTest() {
        AuthorSearchRequest request = new AuthorSearchRequest(null, null, null, LocalDate.now(), LocalDate.now().plusDays(1));
        List<Author> authors = customAuthorRepository.customFilter(request);
        assertAll(
                () -> assertEquals(authors.size(), 3),
                () -> assertTrue(authors.contains(authorSecond)),
                () -> assertTrue(authors.contains(authorSecond)),
                () -> assertTrue(authors.contains(authorThird))
        );
    }

    @Test
    @DisplayName("Фильтр по ФИО + дата")
    void customFilterFullTest() {
        AuthorSearchRequest request = new AuthorSearchRequest("Николай", "Анатольевич", "Петров", LocalDate.now(), LocalDate.now().plusDays(1));
        List<Author> authors = customAuthorRepository.customFilter(request);
        assertAll(
                () -> assertEquals(authors.size(), 1),
                () -> assertTrue(authors.contains(authorThird))
        );
    }

    @Test
    @DisplayName("Фильтр по несуществующим параметрам")
    void customFilterNonexistentTest() {
        AuthorSearchRequest request = new AuthorSearchRequest("Владимир", "Петрович", "Петров", LocalDate.now(), LocalDate.now().plusDays(1));
        List<Author> authors = customAuthorRepository.customFilter(request);
        assertTrue(authors.isEmpty());
    }

}

