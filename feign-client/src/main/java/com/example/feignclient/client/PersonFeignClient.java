package com.example.feignclient.client;

import com.example.feignclient.dto.FullNameDto;
import com.example.feignclient.dto.PersonDto;
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
@RequestMapping("/api/persons")
@FeignClient(name = "main-module")
public interface PersonFeignClient {

    //Показать всех(без книг)
    @GetMapping("/getAll")
    List<PersonDto> getAllPersons();

    //Список, взятых пользователем, книг. Поиск по id пользователя
    @GetMapping("/getBooksByAuthorId")
    PersonDto getBooksByAuthorId(@RequestParam(name = "id") Long id);

    //Добавление нового пользователя
    @PostMapping("/add")
    PersonDto addNewPerson(@RequestBody PersonDto personDto);

    //Удаление данных человека по ФИО
    @DeleteMapping("/deleteByFullName")
    void deletePersonByFullName(FullNameDto firstName);

    //Обновление данных пользователя
    @PutMapping("/update")
    PersonDto updatePerson(@RequestBody PersonDto personDto);

    //Удаление данных пользователя по id
    @DeleteMapping("/deleteById")
    void deletePersonById(@RequestParam(name = "id") Long id);

    //Добавление новой записи в картотеке
    @PostMapping("/addNewPostLibraryCard")
    PersonDto addNewPostLibraryCard(
            @RequestParam(name = "personId") Long personId,
            @RequestParam(name = "bookId") Long bookId);

    //Удаление записи из картотеки
    @DeleteMapping("/deletePostLibraryCard")
    PersonDto deletePostLibraryCard(
            @RequestParam(name = "personId") Long personId,
            @RequestParam(name = "bookId") Long bookId);

    //Список, взятых авторизированнымм пользователем, книг
    @GetMapping("/getListUserBooks")
    PersonDto getListUserBooks();

}
