package com.example.sshomework.api;

import com.example.sshomework.aspect.annotation.LoggerExecuteTime;
import com.example.sshomework.dto.genre.GenreDto;
import com.example.sshomework.dto.genre.GenreStatisticsProjection;
import com.example.sshomework.exception.NotUniqueValueException;
import com.example.sshomework.service.genre.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Aleksey Romodin
 */
@LoggerExecuteTime
@RestController
@RequestMapping(value = "api/genre")
@Tag(name = "Genre", description = "Genre API")
public class GenreRestController {

    private final GenreService genreService;

    @Autowired
    public GenreRestController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(description = "Показать все жанры(без книг)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GenreDto.class)))})
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<GenreDto>> getAll() {
        return ResponseEntity.ok(genreService.getAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Добавление нового жанра(без книги)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись добавлена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenreDto.class))}),
            @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных", content = @Content)
    })
    @PostMapping("/add")
    public void addNewGenre(
            @Parameter(description = "Новый жанр")
            @Valid @RequestBody GenreDto genreDto)
            throws NotUniqueValueException
    {
        genreService.addNewGenre(genreDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(description = "Статистика: жанр -> количество книг")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GenreStatisticsProjection.class)))})
    })
    @GetMapping("/getStatistics")
    public ResponseEntity<List<GenreStatisticsProjection>> getStatistics() {
        return ResponseEntity.ok(genreService.getGenreStatistics());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Удаление жанра по названию.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция завершена", content = @Content),
            @ApiResponse(responseCode = "404", description = "Жанр не найден", content = @Content)
    })
    @DeleteMapping("/delete")
    public void deleteGenre(
            @Parameter(description = "Название жанра")
            @RequestParam(name = "genreName") String genreName)
    {
        genreService.deleteGenre(genreName);
    }
}
