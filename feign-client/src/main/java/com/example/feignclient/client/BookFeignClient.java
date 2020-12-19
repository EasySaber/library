package com.example.feignclient.client;

import com.example.feignclient.dto.FullNameDto;
import com.example.feignclient.dto.book.BookDto;
import com.example.feignclient.dto.book.BookSearchRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@RequestMapping("/api/books")
@FeignClient(name = "main-module")
public interface BookFeignClient {

    //Список книг и фильтром по автору
    @GetMapping("/getByAuthorFilter")
    List<BookDto> getByAuthorFilter(FullNameDto request);

    //Поиск по жанру
    @GetMapping("/getByGenre")
    List<BookDto> getByGenre(@RequestParam(name = "genreName") String genreName);

    //Добавление новой книги
    @PostMapping("/add")
    BookDto addNewBook(@RequestBody BookDto bookDto);

    //Удаление книги по ID
    @DeleteMapping("/delete")
    void deleteBook(@RequestParam(name = "id") Long id);

    //Обновление жанров книги
    @PutMapping("/updateGenres")
    BookDto updateGenres(
            @RequestParam(name = "bookId") Long bookId,
            @RequestParam(name = "genres[]") List<String> genres);

    //Поиск книг по параметрам
    @GetMapping("/getBookInParameter")
    List<BookDto> getBookInParameter(BookSearchRequest request);

}
