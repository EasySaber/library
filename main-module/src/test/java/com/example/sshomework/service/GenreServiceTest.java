package com.example.sshomework.service;

import com.example.sshomework.dto.genre.GenreDto;
import com.example.sshomework.dto.genre.GenreStatisticsProjection;
import com.example.sshomework.entity.Author;
import com.example.sshomework.entity.Book;
import com.example.sshomework.entity.Genre;
import com.example.sshomework.mappers.GenreMapperImpl;
import com.example.sshomework.repository.GenreRepository;
import com.example.sshomework.service.genre.GenreServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

/**
 * @author Aleksey Romodin
 */
@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {
    @Spy
    private GenreMapperImpl genreMapper;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreServiceImpl genreService;

    private static List<Genre> genres;
    private static List<GenreStatisticsProjection> genreStatistics;
    private static List<GenreDto> genresDto;
    private static GenreDto genreDtoFirst;
    private static Genre genreFirst;

    @BeforeAll
    static void setUp() {
        /*
         * Значения по-умолчанию
         * Добавление полной структуры для тестирования маппера
         */
        //Автор
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("Петр");
        author.setMiddleName("Петрович");
        author.setLastName("Петров");
        author.setBooks(new HashSet<>());
        author.setDateTimeOfBirth(LocalDate.parse("1799-06-06"));

        //Книги
        Book bookFirst = new Book();
        bookFirst.setId(1L);
        bookFirst.setBookName("Первая книга");
        bookFirst.setAuthorBook(author);
        bookFirst.setGenres(new HashSet<>());
        bookFirst.setDatePublication(LocalDate.parse("2020-02-01"));

        Book bookSecond = new Book();
        bookSecond.setId(2L);
        bookSecond.setBookName("Вторая книга");
        bookSecond.setAuthorBook(author);
        bookSecond.setGenres(new HashSet<>());
        bookSecond.setDatePublication(LocalDate.parse("2020-02-01"));

        Book bookThird= new Book();
        bookThird.setId(3L);
        bookThird.setBookName("Третья книга");
        bookThird.setAuthorBook(author);
        bookThird.setGenres(new HashSet<>());
        bookThird.setDatePublication(LocalDate.parse("2020-02-01"));

        //Жанры
        genreFirst = new Genre();
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
        author.getBooks().add(bookFirst);
        author.getBooks().add(bookSecond);
        author.getBooks().add(bookThird);
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

        //Формирование списка жанров
        genres = new ArrayList<>();
        genres.add(genreFirst);
        genres.add(genreSecond);
        genres.add(genreThird);

        //Статистика по жанрам
        GenreStatisticsProjection genreStatFirst = new GenreStatisticsProjection() {
            @Override
            public String getGenreName() {
                return genreFirst.getGenreName();
            }

            @Override
            public Long getCount() {
                return (long) genreFirst.getBooks().size();
            }
        };
        GenreStatisticsProjection genreStatSecond = new GenreStatisticsProjection() {
            @Override
            public String getGenreName() {
                return genreSecond.getGenreName();
            }

            @Override
            public Long getCount() {
                return (long) genreSecond.getBooks().size();
            }
        };
        GenreStatisticsProjection genreStatThird = new GenreStatisticsProjection() {
            @Override
            public String getGenreName() {
                return genreThird.getGenreName();
            }

            @Override
            public Long getCount() {
                return (long) genreThird.getBooks().size();
            }
        };

        genreStatistics = new ArrayList<>();
        genreStatistics.add(genreStatFirst);  //Роман - 1
        genreStatistics.add(genreStatSecond); //Рассказ - 2
        genreStatistics.add(genreStatThird);  //Детектив - 1

        //DTO
        genreDtoFirst = new GenreDto(1L, "Роман");
        GenreDto genreDtoSecond = new GenreDto(2L, "Рассказ");
        GenreDto genreDtoThird = new GenreDto(3L,"Детектив");
        genresDto = new ArrayList<>();
        genresDto.add(genreDtoFirst);
        genresDto.add(genreDtoSecond);
        genresDto.add(genreDtoThird);

    }

    @Test
    @DisplayName("Получение всех жанров")
    void getAllTest() {
        when(genreRepository.findAll()).thenReturn(genres);
        assertEquals(genreService.getAll(), genresDto);
        verify(genreRepository, times(1)).findAll();
        verify(genreMapper, times(1)).toDtoList(anyList());
    }

    @Test
    @DisplayName("Получение пустого списка жанров")
    void getAllEmptyTest() {
        when(genreRepository.findAll()).thenReturn(genres);
        genres.clear();
        assertTrue(genreService.getAll().isEmpty());
        verify(genreRepository, times(1)).findAll();
        verify(genreMapper, times(1)).toDtoList(anyList());
    }

    @Test
    @DisplayName("Получение количества книг по каждому жанру(статистика)")
    void getGenreStatisticsTest() {
        when(genreRepository.getGenreStatistics()).thenReturn(genreStatistics);
        assertEquals(genreService.getGenreStatistics(), genreStatistics);
        verify(genreRepository, times(1)).getGenreStatistics();
    }

    @Test
    @DisplayName("Сохранение нового жанра")
    void addNewGenreTest() {
        assertDoesNotThrow(() -> genreService.addNewGenre(genreDtoFirst));
        verify(genreRepository, times(1)).save(new Genre(1L,"Роман", null));
        verify(genreMapper, times(1)).toEntity(any());
    }

    @Test
    @DisplayName("Удаление жанра -> Исключение")
    void  deleteGenreReturnExceptionTest() {
        when(genreRepository.findByGenreName("Детектив")).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class, () -> genreService.deleteGenre("Детектив"));
        assertEquals(throwable.getMessage(), "Жанр не найден." );
        verify(genreRepository, times(0)).delete(any());
    }

    @Test
    @DisplayName("Удаление жанра")
    void deleteGenreTest() {
        when(genreRepository.findByGenreName("Роман")).thenReturn(Optional.of(genreFirst));
        assertDoesNotThrow(() ->genreService.deleteGenre("Роман"));
        verify(genreRepository, times(1)).delete(any());
    }

}
