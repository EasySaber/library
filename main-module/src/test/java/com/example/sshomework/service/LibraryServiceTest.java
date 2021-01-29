package com.example.sshomework.service;

import com.example.sshomework.dto.LibraryCardDto;
import com.example.sshomework.dto.PersonDto;
import com.example.sshomework.dto.author.AuthorDto;
import com.example.sshomework.dto.book.BookDto;
import com.example.sshomework.dto.genre.GenreDto;
import com.example.sshomework.entity.*;
import com.example.sshomework.exception.PersonBookDebtException;
import com.example.sshomework.mappers.Book.BookMapperImpl;
import com.example.sshomework.mappers.LibraryCardMapperImpl;
import com.example.sshomework.repository.LibraryCardRepository;
import com.example.sshomework.repository.PersonRepository;
import com.example.sshomework.repository.book.BookRepository;
import com.example.sshomework.service.libraryCard.LibraryCardServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

/**
 * @author Aleksey Romodin
 */
@ExtendWith(MockitoExtension.class)
public class LibraryServiceTest {

    private final BookMapperImpl bookMapper = new BookMapperImpl();

    @Spy
    private final LibraryCardMapperImpl libraryCardMapper = new LibraryCardMapperImpl(bookMapper);

    @Mock
    private LibraryCardRepository libraryCardRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private LibraryCardServiceImpl libraryCardService;

    private static List<LibraryCard> cards;
    private static List<LibraryCardDto> cardsDto;
    private static LibraryCard libraryCardFirst;
    private static LibraryCard libraryCardSecond;
    private static LibraryCardDto libraryCardDtoFirst;
    private static LibraryCardDto libraryCardDtoSecond;
    private static Person personFirst;
    private static Book bookThird;
    private static PersonDto personDtoFirst;
    private static BookDto bookDtoThird;


    @BeforeAll
    static  void setUp() {
        /*
         * Значения по-умолчанию БД
         * Добавление полной структыру данных для тестирования маппера
         */
        //Пользователи
        personFirst = new Person(1L, "Первый", "Тест", "Тест", LocalDate.parse("2020-02-02"), new HashSet<>(), null);
        Person personSecond= new Person(2L, "Второй", "Тест", "Тест", LocalDate.parse("2020-02-02"), new HashSet<>(), null);

        //Автор
        Author authorFirst = new Author();
        authorFirst.setId(1L);
        authorFirst.setFirstName("Петр");
        authorFirst.setMiddleName("Петрович");
        authorFirst.setLastName("Петров");
        authorFirst.setBooks(new HashSet<>());
        authorFirst.setDateTimeOfBirth(LocalDate.parse("1799-06-06"));

        //Книги
        Book bookFirst = new Book();
        bookFirst.setId(1L);
        bookFirst.setBookName("Первая книга");
        bookFirst.setAuthorBook(authorFirst);
        bookFirst.setGenres(new HashSet<>());
        bookFirst.setDatePublication(LocalDate.parse("2020-02-01"));
        bookFirst.setPersons(new HashSet<>());

        Book bookSecond = new Book();
        bookSecond.setId(2L);
        bookSecond.setBookName("Вторая книга");
        bookSecond.setAuthorBook(authorFirst);
        bookSecond.setGenres(new HashSet<>());
        bookSecond.setPersons(new HashSet<>());
        bookSecond.setDatePublication(LocalDate.parse("2020-02-01"));

        bookThird= new Book();
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

        Genre genreSecond = new Genre();
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

        //Карточки
        libraryCardFirst = new LibraryCard();
        libraryCardFirst.setId(1L);
        libraryCardFirst.setBook(bookFirst);
        libraryCardFirst.setPerson(personFirst);

        libraryCardSecond = new LibraryCard();
        libraryCardSecond.setId(2L);
        libraryCardSecond.setBook(bookSecond);
        libraryCardSecond.setPerson(personSecond);

        //Добавление карточки книгам и пользователям
        personFirst.getBooks().add(libraryCardFirst);
        bookFirst.getPersons().add(libraryCardFirst);

        personSecond.getBooks().add(libraryCardSecond);
        bookSecond.getPersons().add(libraryCardSecond);

        /*
         Значения по-умолчанию DTO
         */
        //Пользователи
        personDtoFirst = new PersonDto(1L, "Первый", "Тест", "Тест", LocalDate.parse("2020-02-02"), new HashSet<>(), null);
        PersonDto personDtoSecond= new PersonDto(2L, "Второй", "Тест", "Тест", LocalDate.parse("2020-02-02"), new HashSet<>(), null);

        //Автор
        AuthorDto authorDtoFirst = new AuthorDto();
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
        bookDtoFirst.setPersonsDto(new HashSet<>());
        bookDtoFirst.setDatePublication(LocalDate.parse("2020-02-01"));
        bookDtoFirst.setPersonsDto(new HashSet<>());

        BookDto bookDtoSecond = new BookDto();
        bookDtoSecond.setId(2L);
        bookDtoSecond.setBookName("Вторая книга");
        bookDtoSecond.setAuthorBookDto(authorDtoFirst);
        bookDtoSecond.setGenres(new HashSet<>());
        bookDtoSecond.setPersonsDto(new HashSet<>());
        bookDtoSecond.setDatePublication(LocalDate.parse("2020-02-01"));

        bookDtoThird= new BookDto();
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

        //Карточки
        libraryCardDtoFirst = new LibraryCardDto();
        libraryCardDtoFirst.setId(1L);
        libraryCardDtoFirst.setBookDto(bookDtoFirst);
        libraryCardDtoFirst.setPersonDto(personDtoFirst);
        libraryCardDtoFirst.setDateTimeReturn(libraryCardFirst.getDateTimeReturn());

        libraryCardDtoSecond = new LibraryCardDto();
        libraryCardDtoSecond.setId(2L);
        libraryCardDtoSecond.setBookDto(bookDtoSecond);
        libraryCardDtoSecond.setPersonDto(personDtoSecond);
        libraryCardDtoSecond.setDateTimeReturn(libraryCardSecond.getDateTimeReturn());

        //Добавление карточки книгам и пользователям
        personDtoFirst.getBooksDto().add(libraryCardDtoFirst);
        bookDtoFirst.getPersonsDto().add(libraryCardDtoFirst);

        personDtoSecond.getBooksDto().add(libraryCardDtoSecond);
        bookDtoSecond.getPersonsDto().add(libraryCardDtoSecond);

        //Списки карточек
        cards = new ArrayList<>();
        cards.add(libraryCardFirst);
        cards.add(libraryCardSecond);

        cardsDto = new ArrayList<>();
        cardsDto.add(libraryCardDtoFirst);
        cardsDto.add(libraryCardDtoSecond);

    }

    @Test
    @DisplayName("Список просроченных возвратов. Пустой список")
    void getDebtorsFalseTest() {
        when(libraryCardRepository.findAll()).thenReturn(cards);
        assertTrue(libraryCardService.getDebtors().isEmpty());
        verify(libraryCardRepository).findAll();
        verify(libraryCardMapper).toDtoList(anyList());
    }

    @Test
    @DisplayName("Список просроченных возвратов. Поиск из списка")
    void getDebtorsTrueTest() {
        ZonedDateTime timeReturn = ZonedDateTime.now().minusDays(10);
        libraryCardFirst.setDateTimeReturn(timeReturn);
        libraryCardSecond.setDateTimeReturn(timeReturn);
        libraryCardDtoFirst.setDateTimeReturn(timeReturn);
        libraryCardDtoSecond.setDateTimeReturn(timeReturn);

        when(libraryCardRepository.findAll()).thenReturn(cards);
        assertEquals(libraryCardService.getDebtors(), cardsDto);
        verify(libraryCardRepository).findAll();
        verify(libraryCardMapper).toDtoList(anyList());
    }

    @Test
    @DisplayName("Пролонгация карточки. Карточка не найдена")
    void prolongationNotFoundTest() {
        when(libraryCardRepository.findByBookIdAndPersonId(1L, 1L)).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class, () -> libraryCardService.prolongation(1L, 1L, 7L));
        assertEquals(throwable.getMessage(), "Карточка не найдена.");
        verify(libraryCardRepository).findByBookIdAndPersonId(1L, 1L);
        verify(libraryCardRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Пролонгация карточки. Сохранение")
    void prolongationSaveTest() {
        ZonedDateTime date = libraryCardFirst.getDateTimeReturn().plusDays(7);
        libraryCardDtoFirst.setDateTimeReturn(date);
        when(libraryCardRepository.findByBookIdAndPersonId(1L, 1L)).thenReturn(Optional.of(libraryCardFirst));
        assertEquals(libraryCardService.prolongation(1L, 1L, 7L), Optional.of(libraryCardDtoFirst));
        verify(libraryCardRepository).findByBookIdAndPersonId(1L, 1L);
        verify(libraryCardRepository).save(any());
        verify(libraryCardMapper).toDto(any());
    }

    @Test
    @DisplayName("Добавление новой карточки. Пользователь не найден")
    void addNewCardNotFoundPersonTest() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class, () -> libraryCardService.addNewCard(1L, 1L));
        assertEquals(throwable.getMessage(), "Пользователь не найден.");
        verify(libraryCardRepository, times(0)).save(any());
        verify(bookRepository, times(0)).findById(anyLong());
        verify(libraryCardRepository, times(0)).findByPersonId(any());
    }

    @Test
    @DisplayName("Добавление новой карточки. Книга не найдена")
    void addNewCardNotFoundBookTest() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(personFirst));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class, () -> libraryCardService.addNewCard(1L, 1L));
        assertEquals(throwable.getMessage(), "Книга не найдена.");
        verify(libraryCardRepository, times(0)).save(any());
        verify(bookRepository).findById(anyLong());
        verify(libraryCardRepository, times(0)).findByPersonId(any());
    }

    @Test
    @DisplayName("Добавление новой карточки. У пользователя имеется задолженность")
    void addNewCardDeptTest() {
        List<LibraryCard> personFirstCards = new ArrayList<>();
        libraryCardFirst.setDateTimeReturn(ZonedDateTime.now().minusDays(10));
        personFirstCards.add(libraryCardFirst);

        when(personRepository.findById(1L)).thenReturn(Optional.of(personFirst));
        when(bookRepository.findById(3L)).thenReturn(Optional.of(bookThird));
        when(libraryCardRepository.findByPersonId(1L)).thenReturn(personFirstCards);
        Throwable throwable = assertThrows(PersonBookDebtException.class, () -> libraryCardService.addNewCard(1L, 3L));
        assertEquals(throwable.getMessage(), "У пользователя имеются задолженности.");
        verify(libraryCardRepository, times(0)).save(any());
        verify(bookRepository).findById(anyLong());
        verify(libraryCardRepository).findByPersonId(any());
    }

    @Test
    @DisplayName("Добавление новой карточки. Сохранение")
    void addNewCardSaveTest() {
        List<LibraryCard> personFirstCards = new ArrayList<>();
        libraryCardFirst.setDateTimeReturn(ZonedDateTime.now().plusDays(10));
        personFirstCards.add(libraryCardFirst);

        LibraryCardDto newCard = new LibraryCardDto(null, bookDtoThird, personDtoFirst, null, ZonedDateTime.now().plusDays(7));

        when(personRepository.findById(1L)).thenReturn(Optional.of(personFirst));
        when(bookRepository.findById(3L)).thenReturn(Optional.of(bookThird));
        when(libraryCardRepository.findByPersonId(1L)).thenReturn(personFirstCards);
        assertEquals(libraryCardService.addNewCard(1L, 3L), Optional.of(newCard));

        verify(libraryCardRepository).save(any());
        verify(bookRepository).findById(anyLong());
        verify(libraryCardRepository).findByPersonId(any());
    }




}
