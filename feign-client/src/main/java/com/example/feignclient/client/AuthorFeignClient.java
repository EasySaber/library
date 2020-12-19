package com.example.feignclient.client;

import com.example.feignclient.dto.author.AuthorDto;
import com.example.feignclient.dto.author.AuthorSearchRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@RequestMapping("/api/author")
@FeignClient(name = "main-module")
public interface AuthorFeignClient {

    //Поиск автора по ID
    @GetMapping("/getById")
    AuthorDto getAuthorById(@RequestParam(name = "id") Long id);

    //Показать всех авторов
    @GetMapping("/getAll")
    List<AuthorDto> getAll();

    //Показать авторов и их книги
    @GetMapping("/getBooksByAuthor")
    List<AuthorDto> getBooksByAuthor();

    //Добавление нового автора
    @PostMapping("/add")
    AuthorDto addNewAuthor(@RequestBody AuthorDto authorDto);

    //Удаление автора по id
    @DeleteMapping("/delete")
    void deleteAuthorById(@RequestParam(name = "id") Long id);

    //Поиск автора по параметрам
    @GetMapping("/getAuthorInParameter")
    List<AuthorDto> getAuthorInParameter(AuthorSearchRequest request);
}
