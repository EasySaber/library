package com.example.sshomework.api;

import com.example.sshomework.dto.genre.GenreDto;
import com.example.sshomework.dto.genre.GenreStatisticsProjection;
import com.example.sshomework.service.genre.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Aleksey Romodin
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/genre")
@Tag(name = "Genre", description = "Genre API")
public class GenreRestController {

    private final GenreService genreService;

    @Operation(description = "Показать все жанры(без книг)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GenreDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        return genreService.getAll().isEmpty() ? ResponseEntity.notFound().build() :
                ResponseEntity.ok(genreService.getAll());
    }

    @Operation(description = "Добавление нового жанра(без книги)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись добавлена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenreDto.class))}),
            @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных", content = @Content)
            })
    @PostMapping("/add")
    public ResponseEntity<Void> addNewGenre(@Parameter(description = "Новый жанр")
                                                @Valid @RequestBody GenreDto genreDto) {
        genreService.addNewGenre(genreDto);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Статистика: жанр -> количество книг")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GenreStatisticsProjection.class)))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/getStatistics")
    public ResponseEntity<?> getStatistics() {
        return genreService.getGenreStatistics().isEmpty() ? ResponseEntity.notFound().build() :
                ResponseEntity.ok(genreService.getGenreStatistics());
    }
}
