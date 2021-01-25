package com.example.sshomework.service;

import com.example.sshomework.dto.author.AuthorDto;
import com.example.sshomework.dto.book.BookDto;
import com.example.sshomework.dto.book.BookStatusDto;
import com.example.sshomework.dto.genre.GenreDto;
import com.example.sshomework.entity.*;
import com.example.sshomework.exception.DeleteRelatedDataException;
import com.example.sshomework.mappers.Book.BookMapperImpl;
import com.example.sshomework.mappers.Book.BookStatusMapperImpl;
import com.example.sshomework.repository.GenreRepository;
import com.example.sshomework.repository.author.AuthorRepository;
import com.example.sshomework.repository.book.BookRepository;
import com.example.sshomework.service.book.BookServiceImpl;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Aleksey Romodin
 */
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Spy
    private BookMapperImpl bookMapper;

    @Spy
    private BookStatusMapperImpl bookStatusMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private List<Book> books;
    private List<BookDto> booksDto;
    private BookDto bookDtoFirst;
    private BookDto bookDtoSecond;
    private Book bookSecond;
    private Book bookFirst;
    private AuthorDto authorDtoFirst;
    private Author authorFirst;
    private Genre genreSecond;
    private GenreDto genreDtoSecond;

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

        bookSecond = new Book();
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
        Genre genreFirst = new Genre();
        genreFirst.setId(1L);
        genreFirst.setGenreName("Роман");
        genreFirst.setBooks(new HashSet<>());

        genreSecond = new Genre();
        genreSecond.setId(2L);
        genreSecond.setGenreName("Рассказ");
        genreSecond.setBooks(new HashSet<>());

        Genre genreThird = new Genre();
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
        books = new ArrayList<>();
        books.add(bookFirst);
        books.add(bookSecond);
        books.add(bookThird);

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
        bookDtoFirst = new BookDto();
        bookDtoFirst.setId(1L);
        bookDtoFirst.setBookName("Первая книга");
        bookDtoFirst.setAuthorBookDto(authorDtoFirst);
        bookDtoFirst.setGenres(new HashSet<>());
        bookDtoFirst.setDatePublication(LocalDate.parse("2020-02-01"));

        bookDtoSecond = new BookDto();
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

        genreDtoSecond = new GenreDto();
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
        booksDto = new ArrayList<>();
        booksDto.add(bookDtoFirst);
        booksDto.add(bookDtoSecond);
        booksDto.add(bookDtoThird);
    }

    @Test
    @DisplayName("Добавление новой книги. Автор не найден")
    void addNewBookNotFoundAuthorTest() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class, () -> bookService.addNewBook(bookDtoSecond));
        assertEquals(throwable.getMessage(), "Автор не найден");
        verify(authorRepository).findById(anyLong());
        verify(bookMapper).toEntity(any());
    }

    @Test
    @DisplayName("Добавление новой книги. Жанры не найдены")
    void addNewBookNotFoundGenresTest() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(authorFirst));
        when(genreRepository.findByGenreName(any())).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class, () -> bookService.addNewBook(bookDtoSecond));
        assertEquals(throwable.getMessage(), "Жанры не найдены.");
        verify(authorRepository).findById(anyLong());
        verify(bookMapper).toEntity(any());
        verify(genreRepository).findByGenreName(any());
    }

    @Test
    @DisplayName("Добавление новой книги. Необходимо добавить жанры")
    void  addNewBookNotFoundInputGenresTest() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(authorFirst));
        Throwable throwable = assertThrows(NotFoundException.class,
                () -> bookService.addNewBook(new BookDto(1L, "Тест", null,authorDtoFirst,null, null)));
        assertEquals(throwable.getMessage(), "Необходимо выбрать жанры.");
        verify(authorRepository).findById(anyLong());
        verify(bookMapper).toEntity(any());
        verify(genreRepository, times(0)).findByGenreName(any());
    }

    @Test
    @DisplayName("Добавление новой книги")
    void addNewBookTest() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(authorFirst));
        when(genreRepository.findByGenreName(any())).thenReturn(Optional.of(genreSecond));
        assertDoesNotThrow(() -> bookService.addNewBook(bookDtoSecond));
        verify(bookRepository).save(bookSecond);
        verify(authorRepository).findById(anyLong());
        verify(bookMapper).toEntity(any());
        verify(genreRepository).findByGenreName(any());
    }

    @Test
    @DisplayName("Удаление книги по id. Книга не найдена")
    void deleteBookByIdNotFoundBookTest() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class, () -> bookService.deleteBookById(2L));
        assertEquals(throwable.getMessage(), "Книга не найдена.");
        verify(bookRepository, times(0)).delete(any());
    }

    @Test
    @DisplayName("Удаление книги по id. Невозможно удалить книгу, находится в пользовании")
    void deleteBookByIdDeleteRelatedDataExceptionTest() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookFirst));
        Throwable throwable = assertThrows(DeleteRelatedDataException.class, () -> bookService.deleteBookById(1L));
        assertEquals(throwable.getMessage(), "Невозможно удалить книгу, находится в пользовании.");
        verify(bookRepository, times(0)).delete(any());
    }

    @Test
    @DisplayName("Удаление книги по id")
    void deleteBookByIdTest() {
        when(bookRepository.findById(2L)).thenReturn(Optional.of(bookSecond));
        assertDoesNotThrow(() -> bookService.deleteBookById(2L));
        verify(bookRepository).delete(bookSecond);
    }

    @Test
    @DisplayName("Поиск по жанру. Жанр не найден")
    void getByGenreNotFoundTest() {
        when(genreRepository.findByGenreName(any())).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class, () -> bookService.getByGenre("Роман"));
        assertEquals(throwable.getMessage(), "Жанр не найден.");
        verify(genreRepository).findByGenreName(any());
    }

    @Test
    @DisplayName("Поиск по жанру. Найдено 2 совпрадения")
    void getByGenreListTest() {
        when(genreRepository.findByGenreName("Рассказ")).thenReturn(Optional.of(genreSecond));
        when(bookRepository.findAll()).thenReturn(books);
        booksDto.remove(bookDtoFirst);
        assertEquals(bookService.getByGenre("Рассказ"), booksDto);
        verify(genreRepository).findByGenreName(any());
        verify(bookRepository).findAll();
    }

    @Test
    @DisplayName("Поиск по жанру. Пустой список, совпадений не найдено")
    void getByGenreListEmptyTest() {
        when(genreRepository.findByGenreName("Быль")).thenReturn(Optional.of(new Genre(5L, "Быль", null)));
        when(bookRepository.findAll()).thenReturn(books);
        assertTrue(bookService.getByGenre("Быль").isEmpty());
        verify(genreRepository).findByGenreName(any());
        verify(bookRepository).findAll();
    }

    @Test
    @DisplayName("Обновление жанров книги. Жанры не найдены")
    void updateGenresNotFoundGenresTest() {
        List<String> genresNameList = new ArrayList<>();
        Throwable throwable = assertThrows(NotFoundException.class, () -> bookService.updateGenres(1L, genresNameList));
        assertEquals(throwable.getMessage(),"Жанры не найдены.");
        verify(bookRepository, times(0)).findById(anyLong());
        verify(bookRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Обновление жанров книги. Книга не найдена")
    void updateGenresNotFoundBookTest() {
        List<String> genresNameList = new ArrayList<>();
        genresNameList.add("Быль");
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class, () -> bookService.updateGenres(1L, genresNameList));
        assertEquals(throwable.getMessage(),"Книга не найдена.");
        verify(bookRepository).findById(anyLong());
        verify(bookRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Обновление жанров книги. Жанры не найдены в БД")
    void updateGenresNotFoundGenreInTableTest() {
        List<String> genresNameList = new ArrayList<>();
        genresNameList.add("Быль");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookFirst));
        when(genreRepository.findByGenreName("Быль")).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class, () -> bookService.updateGenres(1L, genresNameList));
        assertEquals(throwable.getMessage(),"Жанры не найдены.");
        verify(bookRepository).findById(anyLong());
        verify(bookRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Обновление жанров книги.Сохранение")
    void updateGenresTest() {
        List<String> genresNameList = new ArrayList<>();
        genresNameList.add("Рассказ");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookFirst));
        when(genreRepository.findByGenreName("Рассказ")).thenReturn(Optional.of(genreSecond));

        BookDto saveBookDto = bookService.updateGenres(1L, genresNameList).orElseThrow(() -> new NotFoundException("Ошибка при сохранениии"));
        assertEquals(saveBookDto, bookDtoFirst);

        Set<GenreDto> saveGenreList = new HashSet<>();
        saveGenreList.add(genreDtoSecond);
        assertEquals(saveBookDto.getGenres(), saveGenreList);

        verify(bookRepository).findById(anyLong());
        verify(bookRepository).save(any());
    }

    @Test
    @DisplayName("Список книг и статус. Пустой список")
    void getBooksReturnEmptyListTest() {
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(bookService.getBooks().isEmpty());
        verify(bookRepository).findAll();
    }

    @Test
    @DisplayName("Список книг и статус")
    void getBooks() {
        BookStatusDto bookStatusDtoFirst = new BookStatusDto(1L, "Первая книга", "Петр Петрович Петров", "Роман", LocalDate.parse("2020-02-01"), "В пользовании");
        BookStatusDto bookStatusDtoSecond = new BookStatusDto(2L, "Вторая книга", "Петр Петрович Петров", "Рассказ", LocalDate.parse("2020-02-01"), "Свободна");
        BookStatusDto bookStatusDtoThird = new BookStatusDto(3L, "Третья книга", "Петр Петрович Петров", "Рассказ, Детектив", LocalDate.parse("2020-02-01"), "Свободна");
        List<BookStatusDto> booksStatusDto = new ArrayList<>();
        booksStatusDto.add(bookStatusDtoFirst);
        booksStatusDto.add(bookStatusDtoSecond);
        booksStatusDto.add(bookStatusDtoThird);

        when(bookRepository.findAll()).thenReturn(books);
        assertEquals(bookService.getBooks(), booksStatusDto);
        verify(bookRepository).findAll();
    }


}
