package com.example.sshomework.service;

import com.example.sshomework.dto.author.AuthorDto;
import com.example.sshomework.dto.book.BookDto;
import com.example.sshomework.dto.genre.GenreDto;
import com.example.sshomework.entity.*;
import com.example.sshomework.exception.DeleteRelatedDataException;
import com.example.sshomework.mappers.AuthorMapperImpl;
import com.example.sshomework.repository.GenreRepository;
import com.example.sshomework.repository.author.AuthorRepository;
import com.example.sshomework.service.author.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Aleksey Romodin
 */
@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
    @Spy
    private AuthorMapperImpl authorMapper;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author authorFirst;
    private AuthorDto authorDtoFirst;
    private List<Author> authors;
    private List<AuthorDto> authorsDto;
    private Genre genreFirst;
    private Genre genreSecond;
    private Genre genreThird;
    private Book bookFirst;

    @BeforeEach
    void setUp() {
        /*
         * Значения по-умолчанию БД
         * Добавление полной структыру данных для тестирования маппера
         */
        //Автор
        authorFirst = new Author();
        authorFirst.setId(1L);
        authorFirst.setFirstName("Петр");
        authorFirst.setMiddleName("Петрович");
        authorFirst.setLastName("Петров");
        authorFirst.setBooks(new HashSet<>());
        authorFirst.setDateTimeOfBirth(LocalDate.parse("1799-06-06"));

        //Книги
        bookFirst = new Book();
        bookFirst.setId(1L);
        bookFirst.setBookName("Первая книга");
        bookFirst.setAuthorBook(authorFirst);
        bookFirst.setGenres(new HashSet<>());
        bookFirst.setDatePublication(LocalDate.parse("2020-02-01"));
        bookFirst.setPersons(new HashSet<>());

        Person personTest = new Person(1L, "Тест", "Тест", "Тест", null, null, null);
        LibraryCard libraryCard = new LibraryCard(1L, bookFirst, personTest, null);
        bookFirst.getPersons().add(libraryCard);

        Book bookSecond = new Book();
        bookSecond.setId(2L);
        bookSecond.setBookName("Вторая книга");
        bookSecond.setAuthorBook(authorFirst);
        bookSecond.setGenres(new HashSet<>());
        bookSecond.setDatePublication(LocalDate.parse("2020-02-01"));

        Book bookThird= new Book();
        bookThird.setId(3L);
        bookThird.setBookName("Третья книга");
        bookThird.setAuthorBook(authorFirst);
        bookThird.setGenres(new HashSet<>());
        bookThird.setDatePublication(LocalDate.parse("2020-02-01"));

        //Жанры
        genreFirst = new Genre();
        genreFirst.setId(1L);
        genreFirst.setGenreName("Роман");
        genreFirst.setBooks(new HashSet<>());

        genreSecond = new Genre();
        genreSecond.setId(2L);
        genreSecond.setGenreName("Рассказ");
        genreSecond.setBooks(new HashSet<>());

        genreThird = new Genre();
        genreThird.setId(3L);
        genreThird.setGenreName("Детектив");
        genreThird.setBooks(new HashSet<>());

        //Добавление книг автору
        authorFirst.getBooks().add(bookFirst);
        authorFirst.getBooks().add(bookSecond);
        authorFirst.getBooks().add(bookThird);
        //Добавление книг жанрам
        genreFirst.getBooks().add(bookFirst);
        genreSecond.getBooks().add(bookSecond);
        genreSecond.getBooks().add(bookThird);
        genreThird.getBooks().add(bookThird);
        //Добавление жанров книгам
        bookFirst.getGenres().add(genreFirst);
        bookSecond.getGenres().add(genreSecond);
        bookThird.getGenres().add(genreSecond);
        bookThird.getGenres().add(genreThird);

        //Список
        authors = new ArrayList<>();
        authors.add(authorFirst);
        authors.add(new Author(2L, "Тест", "Тест", "Тест", null, LocalDate.parse("2020-02-02")));
        authors.add(new Author(3L, "Тест", "Тест", "Тест", null, LocalDate.parse("2020-02-02")));

        /*
         Значения по-умолчанию DTO
         */
        //Автор
        authorDtoFirst = new AuthorDto();
        authorDtoFirst.setId(1L);
        authorDtoFirst.setFirstName("Петр");
        authorDtoFirst.setMiddleName("Петрович");
        authorDtoFirst.setLastName("Петров");
        authorDtoFirst.setBooks(new HashSet<>());
        authorDtoFirst.setDateTimeOfBirth(LocalDate.parse("1799-06-06"));

        //Книги
        BookDto bookDtoFirst = new BookDto();
        bookDtoFirst.setId(1L);
        bookDtoFirst.setBookName("Первая книга");
        bookDtoFirst.setAuthorBookDto(authorDtoFirst);
        bookDtoFirst.setGenres(new HashSet<>());
        bookDtoFirst.setDatePublication(LocalDate.parse("2020-02-01"));

        BookDto bookDtoSecond = new BookDto();
        bookDtoSecond.setId(2L);
        bookDtoSecond.setBookName("Вторая книга");
        bookDtoSecond.setAuthorBookDto(authorDtoFirst);
        bookDtoSecond.setGenres(new HashSet<>());
        bookDtoSecond.setDatePublication(LocalDate.parse("2020-02-01"));

        BookDto bookDtoThird= new BookDto();
        bookDtoThird.setId(3L);
        bookDtoThird.setBookName("Третья книга");
        bookDtoThird.setAuthorBookDto(authorDtoFirst);
        bookDtoThird.setGenres(new HashSet<>());
        bookDtoThird.setDatePublication(LocalDate.parse("2020-02-01"));

        //Жанры
        GenreDto genreDtoFirst = new GenreDto();
        genreDtoFirst.setId(1L);
        genreDtoFirst.setGenreName("Роман");

        GenreDto genreDtoSecond = new GenreDto();
        genreDtoSecond.setId(2L);
        genreDtoSecond.setGenreName("Рассказ");

        GenreDto genreDtoThird = new GenreDto();
        genreDtoThird.setId(3L);
        genreDtoThird.setGenreName("Детектив");

        //Добавление книг автору
        authorDtoFirst.getBooks().add(bookDtoFirst);
        authorDtoFirst.getBooks().add(bookDtoSecond);
        authorDtoFirst.getBooks().add(bookDtoThird);

        //Добавление жанров книгам
        bookDtoFirst.getGenres().add(genreDtoFirst);
        bookDtoSecond.getGenres().add(genreDtoSecond);
        bookDtoThird.getGenres().add(genreDtoSecond);
        bookDtoThird.getGenres().add(genreDtoThird);

        //Список
        authorsDto = new ArrayList<>();
        authorsDto.add(authorDtoFirst);
        authorsDto.add(new AuthorDto(2L, "Тест", "Тест", "Тест", null, LocalDate.parse("2020-02-02"), null));
        authorsDto.add(new AuthorDto(3L, "Тест", "Тест", "Тест", null, LocalDate.parse("2020-02-02"), null));
    }

    @Test
    @DisplayName("Поиск автора по id -> пустое значение")
    void getAuthorByIdReturnEmptyTest() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        assertEquals(authorService.getAuthorById(1L), Optional.empty());
        verify(authorRepository, times(1)).findById(anyLong());
        verify(authorMapper, times(0)).toDto(any());
    }

    @Test
    @DisplayName("Поиск автора по id")
    void getAuthorByIdReturnDtoTest() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(authorFirst));
        assertEquals(authorService.getAuthorById(1L), Optional.of(authorDtoFirst));
        verify(authorRepository, times(1)).findById(anyLong());
        verify(authorMapper, times(1)).toDto(any());
    }

    @Test
    @DisplayName("Получение пустого списка авторов")
    void getAllReturnEmptyTest() {
        when(authorRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(authorService.getAll().isEmpty());
        verify(authorRepository, times(1)).findAll();
        verify(authorMapper, times(1)).toDtoList(anyList());
    }

    @Test
    @DisplayName("Получение списка авторов")
    void getAllTest() {
        when(authorRepository.findAll()).thenReturn(authors);
        assertEquals(authorService.getAll(), authorsDto);
        verify(authorRepository, times(1)).findAll();
        verify(authorMapper, times(1)).toDtoList(anyList());
    }

    @Test
    @DisplayName("Добавление нового автора")
    void addNewAuthorWithBookWithGenre() {
        when(genreRepository.findByGenreName("Роман")).thenReturn(Optional.of(genreFirst));
        when(genreRepository.findByGenreName("Рассказ")).thenReturn(Optional.of(genreSecond));
        when(genreRepository.findByGenreName("Детектив")).thenReturn(Optional.of(genreThird));
        when(authorRepository.findFirstByOrderByIdDesc()).thenReturn(Optional.of(authorFirst));
        authorService.addNewAuthor(authorDtoFirst);
        verify(authorRepository, times(1)).save(authorFirst);
        verify(genreRepository, times(4)).findByGenreName(any());
        verify(authorMapper, times(1)).toEntity(any());
    }

    @Test
    @DisplayName("Автор не найден")
    void deleteAuthorByIdNotFoundTest() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> authorService.deleteAuthorById(1L));
        verify(authorRepository,times(1)).findById(anyLong());
        verify(authorMapper,times(0)).toDto(any());
    }

    @Test
    @DisplayName("Удаление невозможно. Книги автора находяться в пользовании")
    void deleteAuthorByIdNotDeleteTest() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(authorFirst));
        assertThrows(DeleteRelatedDataException.class, () -> authorService.deleteAuthorById(1L));
        verify(authorRepository,times(1)).findById(anyLong());
        verify(authorMapper,times(0)).toDto(any());
    }

    @Test
    @DisplayName("Удаление автора по id")
    void deleteAuthorByIdTest() {
        bookFirst.getPersons().clear();
        when(authorRepository.findById(1L)).thenReturn(Optional.of(authorFirst));
        assertDoesNotThrow(() -> authorService.deleteAuthorById(1L));
        verify(authorRepository,times(1)).findById(anyLong());
        verify(authorRepository,times(1)).delete(authorFirst);
    }








}
