package com.example.sshomework.api;

import com.example.sshomework.dto.LibraryCard;
import com.example.sshomework.service.libraryCard.LibraryCardService;
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

    @Operation(description = "Добавление новой записи в учетной книге")
    @ApiResponse(responseCode = "200", description = "Запись добавлена",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = LibraryCard.class)))})

    @PostMapping("/add")
    public ResponseEntity<List<LibraryCard>> addNewRecord(@Parameter(description = "Новая запись в картотеке")
                                              @RequestBody LibraryCard libraryCard) {
        libraryCardService.addNewRecord(libraryCard);
        return ResponseEntity.ok(libraryCardService.getAll());
    }
}
