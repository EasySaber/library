package com.example.sshomework.api;

import com.example.sshomework.aspect.annotation.LoggerExecuteTime;
import com.example.sshomework.dto.LibraryCardDto;
import com.example.sshomework.dto.view.View;
import com.example.sshomework.exception.PersonBookDebtException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author Aleksey Romodin
 */
@LoggerExecuteTime
@RestController
@RequestMapping(value = "api/libraryCard")
@Tag(name = "LibraryCard", description = "LibraryCard API")
public class LibraryCardRestController {

    private final LibraryCardService libraryCardService;

    @Autowired
    public LibraryCardRestController(LibraryCardService libraryCardService) {
        this.libraryCardService = libraryCardService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(View.LibraryCard.class)
    @Operation(description = "Показать всех задолжников")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LibraryCardDto.class)))})
    })
    @GetMapping("/getDebtors")
    public ResponseEntity<List<LibraryCardDto>> getDebtors() {
        return ResponseEntity.ok(libraryCardService.getDebtors());
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
    public ResponseEntity<LibraryCardDto> prolongation(
            @Parameter(description = "Id пользователя")
            @RequestParam(name = "personId") Long personId,
            @Parameter(description = "Id книги")
            @RequestParam(name = "bookId") Long bookId,
            @Parameter(description = "Количество дней")
            @RequestParam(name = "days") @Positive Long days)
    {
        return ResponseEntity.of(libraryCardService.prolongation(personId, bookId, days));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(View.LibraryCard.class)
    @Operation(description = "Получение новой книги")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Книга получена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LibraryCardDto.class))}),
            @ApiResponse(responseCode = "404", description = "Запись не найдена.", content = @Content),
            @ApiResponse(responseCode = "423", description = "Имеется задолженность.", content = @Content)
    })
    @PostMapping("/add")
    public ResponseEntity<LibraryCardDto> addNewCard(
            @Parameter(description = "Id пользователя")
            @RequestParam(name = "personId") Long personId,
            @Parameter(description = "Id книги")
            @RequestParam(name = "bookId") Long bookId)
            throws PersonBookDebtException
    {
        return ResponseEntity.of(libraryCardService.addNewCard(personId, bookId));
    }


}