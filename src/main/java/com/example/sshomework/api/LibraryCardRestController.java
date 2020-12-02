package com.example.sshomework.api;

import com.example.sshomework.dto.LibraryCardDto;
import com.example.sshomework.dto.view.View;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/libraryCard")
@Tag(name = "LibraryCard", description = "LibraryCard API")
public class LibraryCardRestController {

    private final LibraryCardService libraryCardService;

    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(View.LibraryCard.class)
    @Operation(description = "Показать всех задолжников")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LibraryCardDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/getDebtors")
    public ResponseEntity<?> getDebtors() {
        List<LibraryCardDto> libraryCards = libraryCardService.getDebtors();
        return libraryCards.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(libraryCards);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(View.LibraryCard.class)
    @Operation(description = "Продление сроков")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные обновлены",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LibraryCardDto.class))}),
            @ApiResponse(responseCode = "404", description = "Ошибка в переданных данных", content = @Content)
    })
    @PutMapping("/prolongation")
    public ResponseEntity<?> prolongation(@Parameter(description = "Id пользователя")
                                          @RequestParam(name = "personId") Long personId,
                                          @Parameter(description = "Id книги")
                                          @RequestParam(name = "bookId") Long bookId,
                                          @Parameter(description = "Количество дней")
                                          @RequestParam(name = "days") Long days) {
        LibraryCardDto libraryCardDto = libraryCardService.prolongation(personId, bookId, days);
        return libraryCardDto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(libraryCardDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(View.LibraryCard.class)
    @Operation(description = "Получение новой книги")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Книга получена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LibraryCardDto.class))}),
            @ApiResponse(responseCode = "404", description = "Пользователь имеет просроченный возврат книги.", content = @Content)
    })
    @PostMapping("/add")
    public ResponseEntity<?> addNewCard(@Parameter(description = "Id пользователя")
                                        @RequestParam(name = "personId") Long personId,
                                        @Parameter(description = "Id книги")
                                        @RequestParam(name = "bookId") Long bookId) {
        LibraryCardDto libraryCardDto = libraryCardService.addNewCard(personId, bookId);
        return libraryCardDto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(libraryCardDto);
    }


}