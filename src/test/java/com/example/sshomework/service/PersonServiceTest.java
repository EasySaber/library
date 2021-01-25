package com.example.sshomework.service;

import com.example.sshomework.dto.FullNameDto;
import com.example.sshomework.dto.LibraryCardDto;
import com.example.sshomework.dto.PersonDto;
import com.example.sshomework.dto.author.AuthorDto;
import com.example.sshomework.dto.book.BookDto;
import com.example.sshomework.dto.genre.GenreDto;
import com.example.sshomework.entity.*;
import com.example.sshomework.mappers.Book.BookMapperImpl;
import com.example.sshomework.mappers.LibraryCardMapperImpl;
import com.example.sshomework.mappers.PersonMapperImpl;
import com.example.sshomework.repository.PersonRepository;
import com.example.sshomework.repository.book.BookRepository;
import com.example.sshomework.service.person.PersonServiceImpl;
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
public class PersonServiceTest {

    private final BookMapperImpl bookMapper = new BookMapperImpl();

    private final LibraryCardMapperImpl libraryCardMapper = new LibraryCardMapperImpl(bookMapper);

    @Spy
    private final PersonMapperImpl personMapper = new PersonMapperImpl(libraryCardMapper);

    @Mock
    private PersonRepository personRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    private static List<Person> persons;
    private static List<PersonDto> personsDto;
    private static Person personFirst;
    private static PersonDto personDtoFirst;
    private static Book bookThird;
    private static BookDto bookDtoThird;
    private static Book bookFirst;
    private static Person personThird;

    @BeforeAll
    static void setUp() {
        /*
         * Значения по-умолчанию БД
         * Добавление полной структыру данных для тестирования маппера
         */
        //Пользователи
        personFirst = new Person(1L, "Первый", "Тест", "Тест", LocalDate.parse("2020-02-02"), new HashSet<>(), null);
        Person personSecond= new Person(2L, "Второй", "Тест", "Тест", LocalDate.parse("2020-02-02"), new HashSet<>(), null);
        personThird = new Person(3L, "Третий", "Тест", "Тест", LocalDate.parse("2020-02-02"), null, null);

        persons = new ArrayList<>();
        persons.add(personFirst);
        persons.add(personSecond);
        persons.add(personThird);

        //Автор
        Author authorFirst = new Author();
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

        Book bookSecond = new Book();
        bookSecond.setId(2L);
        bookSecond.setBookName("Вторая книга");
        bookSecond.setAuthorBook(authorFirst);
        bookSecond.setGenres(new HashSet<>());
        bookSecond.setPersons(new HashSet<>());
        bookSecond.setDatePublication(LocalDate.parse("2020-02-01"));

        bookThird = new Book();
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
        LibraryCard libraryCardFirst = new LibraryCard();
        libraryCardFirst.setId(1L);
        libraryCardFirst.setBook(bookFirst);
        libraryCardFirst.setPerson(personFirst);

        LibraryCard libraryCardSecond = new LibraryCard();
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
        PersonDto personDtoThird = new PersonDto(3L, "Третий", "Тест", "Тест", LocalDate.parse("2020-02-02"), null, null);

        //Список пользователей
        personsDto = new ArrayList<>();
        personsDto.add(personDtoFirst);
        personsDto.add(personDtoSecond);
        personsDto.add(personDtoThird);

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

        bookDtoThird = new BookDto();
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
        LibraryCardDto libraryCardDtoFirst = new LibraryCardDto();
        libraryCardDtoFirst.setId(1L);
        libraryCardDtoFirst.setBookDto(bookDtoFirst);
        libraryCardDtoFirst.setPersonDto(personDtoFirst);
        libraryCardDtoFirst.setDateTimeReturn(libraryCardFirst.getDateTimeReturn());

        LibraryCardDto libraryCardDtoSecond = new LibraryCardDto();
        libraryCardDtoSecond.setId(2L);
        libraryCardDtoSecond.setBookDto(bookDtoSecond);
        libraryCardDtoSecond.setPersonDto(personDtoSecond);
        libraryCardDtoSecond.setDateTimeReturn(libraryCardSecond.getDateTimeReturn());

        //Добавление карточки книгам и пользователям
        personDtoFirst.getBooksDto().add(libraryCardDtoFirst);
        bookDtoFirst.getPersonsDto().add(libraryCardDtoFirst);

        personDtoSecond.getBooksDto().add(libraryCardDtoSecond);
        bookDtoSecond.getPersonsDto().add(libraryCardDtoSecond);

    }

    @Test
    @DisplayName("Список всех пользователей. Пустой список")
    void getAllEmptyListTest() {
        when(personRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(personService.getAll().isEmpty());
        verify(personRepository).findAll();
        verify(personMapper).toDtoList(anyList());
    }

    @Test
    @DisplayName("Список всех пользователей")
    void getAllTest() {
        when(personRepository.findAll()).thenReturn(persons);
        assertEquals(personService.getAll(), personsDto);
        verify(personRepository).findAll();
        verify(personMapper).toDtoList(anyList());
    }

    @Test
    @DisplayName("Удаление пользователей по совпадению ФИО. Совпадения не найдены")
    void deletePersonsByFullNameNotFoundTest() {
        FullNameDto fullName = new FullNameDto("Первый", "Тест", "Тест");
        when(personRepository.findByFirstNameAndMiddleNameAndLastName(fullName.getFirstName(), fullName.getMiddleName(), fullName.getLastName())).thenReturn(new ArrayList<>());
        Throwable throwable = assertThrows(NotFoundException.class, () -> personService.deletePersonsByFullName(fullName));
        assertEquals(throwable.getMessage(), "Совпадения не найдены.");
        verify(personRepository, times(0)).delete(any());
    }

    @Test
    @DisplayName("Удаление пользователей по совпадению ФИО")
    void deletePersonsByFullNameTest() {
        FullNameDto fullName = new FullNameDto("Первый", "Тест", "Тест");
        List<Person> personList = new ArrayList<>();
        personList.add(personFirst);

        when(personRepository.findByFirstNameAndMiddleNameAndLastName(fullName.getFirstName(), fullName.getMiddleName(), fullName.getLastName())).thenReturn(personList);
        assertDoesNotThrow(() -> personService.deletePersonsByFullName(fullName));
        verify(personRepository).deleteAll(personList);
    }

    @Test
    @DisplayName("Получение информации о пользователе по id. Пользователь не найден")
    void getBooksByAuthorNotFoundIdTest() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class, () -> personService.getBooksByAuthorId(1L));
        assertEquals(throwable.getMessage(), "Пользователь не найден.");
        verify(personRepository).findById(anyLong());
        verify(personMapper, times(0)).toDto(any());
    }

    @Test
    @DisplayName("Получение информации о пользователе по id")
    void getBooksByAuthorIdTest() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(personFirst));
        assertEquals(personService.getBooksByAuthorId(1L), Optional.of(personDtoFirst));
        verify(personRepository).findById(anyLong());
        verify(personMapper).toDto(any());
    }

    @Test
    @DisplayName("Добавление нового пользователя")
    void addNewPersonTest() {
        personService.addNewPerson(personDtoFirst);
        verify(personRepository).save(personFirst);
    }

    @Test
    @DisplayName("Обновление данных пользователя. Пользователь не найден")
    void updatePersonNotFoundTest() {
        PersonDto updatePerson = new PersonDto(1L, "Update", "Update", "Update", LocalDate.parse("2011-01-01"), null, null);
        when(personRepository.findById(1L)).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class, () -> personService.updatePerson(updatePerson));
        assertEquals(throwable.getMessage(), "Пользователь не найден.");
        verify(personRepository).findById(anyLong());
        verify(personMapper, times(0)).toDto(any());
        verify(personRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Обновление данных пользователя")
    void updatePersonTest() {
        PersonDto updatePerson = new PersonDto(1L, "Update", "Update", "Update", LocalDate.parse("2011-01-01"), null, null);
        personDtoFirst.setFirstName("Update");
        personDtoFirst.setMiddleName("Update");
        personDtoFirst.setLastName("Update");
        personDtoFirst.setDateOfBirth(LocalDate.parse("2011-01-01"));

        when(personRepository.findById(1L)).thenReturn(Optional.of(personFirst));
        assertEquals(personService.updatePerson(updatePerson), Optional.of(personDtoFirst));
        verify(personRepository).findById(anyLong());
        verify(personMapper).toDto(any());
        verify(personRepository).save(personFirst);
    }

    @Test
    @DisplayName("Удаление данных пользователя. Пользователь не найден")
    void deletePersonByIdNotFoundTest() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class, () -> personService.deletePersonById(1L));
        assertEquals(throwable.getMessage(), "Пользователь не найден.");
        verify(personRepository).findById(anyLong());
        verify(personRepository, times(0)).delete(any());
    }

    @Test
    @DisplayName("Удаление данных пользователя")
    void deletePersonByIdTest() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(personFirst));
        assertDoesNotThrow(() -> personService.deletePersonById(1L));
        verify(personRepository).findById(anyLong());
        verify(personRepository).delete(personFirst);
    }

    @Test
    @DisplayName("Добавление новой книги пользователю. Пользователь не найден")
    void addNewPostLibraryCardNotFoundPersonTest() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class, () -> personService.addNewPostLibraryCard(1L, 3L));
        assertEquals(throwable.getMessage(), "Пользователь не найден.");
        verify(personRepository).findById(anyLong());
        verify(personRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Добавление новой книги пользователю. Книга не найдена")
    void addNewPostLibraryCardNotFoundBookTest() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(personFirst));
        when(bookRepository.findById(3L)).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class, () -> personService.addNewPostLibraryCard(1L, 3L));
        assertEquals(throwable.getMessage(), "Книга не найдена.");
        verify(personRepository).findById(anyLong());
        verify(personRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Добавление новой книги пользователю")
    void addNewPostLibraryCardTest() {
        LibraryCardDto newCard = new LibraryCardDto(null, bookDtoThird, personDtoFirst, null, null);
        personDtoFirst.getBooksDto().add(newCard);
        when(personRepository.findById(1L)).thenReturn(Optional.of(personFirst));
        when(bookRepository.findById(3L)).thenReturn(Optional.of(bookThird));
        assertEquals(personService.addNewPostLibraryCard(1L, 3L), Optional.of(personDtoFirst));
        verify(personRepository).findById(anyLong());
        verify(personRepository).save(any());
    }

    @Test
    @DisplayName("Удаление книги у пользователя. Пользователь не найден")
    void deletePostLibraryCardNotFoundPersonTest() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class, () -> personService.deletePostLibraryCard(1L, 1L));
        assertEquals(throwable.getMessage(), "Пользователь не найден.");
        verify(personRepository).findById(anyLong());
        verify(personRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Удаление книги у пользователя. Книга не найдена")
    void deletePostLibraryCardNotFoundBookTest() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(personFirst));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class, () -> personService.deletePostLibraryCard(1L, 1L));
        assertEquals(throwable.getMessage(), "Книга не найдена.");
        verify(personRepository).findById(anyLong());
        verify(bookRepository).findById(anyLong());
        verify(personRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Удаление книги у пользователя. Нет карточек")
    void deletePostLibraryCardNotFoundLibraryCardTest() {
        when(personRepository.findById(3L)).thenReturn(Optional.of(personThird));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookFirst));
        Throwable throwable = assertThrows(NotFoundException.class, () -> personService.deletePostLibraryCard(3L, 1L));
        assertEquals(throwable.getMessage(), "У пользователя нет взятых книг.");
        verify(personRepository).findById(anyLong());
        verify(bookRepository).findById(anyLong());
        verify(personRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Удаление книги у пользователя")
    void deletePostLibraryCardTest() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(personFirst));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookFirst));
        Optional<PersonDto> save = assertDoesNotThrow(() -> personService.deletePostLibraryCard(1L, 1L));
        assertEquals(save, Optional.of(personDtoFirst));
        verify(personRepository).findById(anyLong());
        verify(bookRepository).findById(anyLong());
        verify(personRepository).save(personFirst);
    }
}
