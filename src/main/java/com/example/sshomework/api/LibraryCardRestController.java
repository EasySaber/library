package com.example.sshomework.api;

import com.example.sshomework.dto.LibraryCard;
import com.example.sshomework.dto.View;
import com.example.sshomework.service.libraryCard.LibraryCardService;
import com.fasterxml.jackson.annotation.JsonView;
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
import java.util.List;

/**
 * @author Aleksey Romodin
 */
@RestController
@RequestMapping("/api/LibraryCard")
@RequiredArgsConstructor
@Tag(name = "LibraryCard", description = "LibraryCard API")
public class LibraryCardRestController {

    private final LibraryCardService libraryCardService;

    @JsonView(View.All.class)
    @Operation(description = "Показать все записи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LibraryCard.class)))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<LibraryCard>> getAll() {
        return libraryCardService.getAll().isEmpty() ?
                ResponseEntity.notFound().build() : ResponseEntity.ok(libraryCardService.getAll());
    }

    @JsonView(View.All.class)
    @Operation(description = "Добавление новой записи в учетной книге")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись добавлена",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LibraryCard.class)))}),
            @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных", content = @Content)
    })

    @PostMapping("/add")
    public ResponseEntity<List<LibraryCard>> addNewRecord(@Parameter(description = "Новая запись в картотеке")
                                                              @Valid @RequestBody LibraryCard libraryCard) {
        libraryCardService.addNewRecord(libraryCard);
        return ResponseEntity.ok(libraryCardService.getAll());
    }
}
