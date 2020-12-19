package com.example.feignclient.client;

import com.example.feignclient.dto.genre.GenreDto;
import com.example.feignclient.dto.genre.GenreStatisticsProjection;
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
@RequestMapping("/api/genre")
@FeignClient(name = "main-module")
public interface GenreFeignClient {

    //Показать все жанры(без книг)
    @GetMapping("/getAll")
    List<GenreDto> getAll();

    //Добавление нового жанра(без книги)
    @PostMapping("/add")
    void addNewGenre(@RequestBody GenreDto genreDto);

    //Статистика: жанр -> количество книг
    @GetMapping("/getStatistics")
    List<GenreStatisticsProjection> getStatistics();

    //Удаление жанра по названию.
    @DeleteMapping("/delete")
    void deleteGenre(@RequestParam(name = "genreName") String genreName);
}
