package com.example.sshomework.api;

import com.example.sshomework.aspect.annotation.LoggerExecuteTime;
import com.example.sshomework.dto.history.HistoryDto;
import com.example.sshomework.service.history.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author Aleksey Romodin
 */
@LoggerExecuteTime
@RestController
@PreAuthorize("hasAnyRole('ADMIN')")
@RequestMapping(value = "api/history")
@Tag(name = "History", description = "History API")
public class HistoryRestController {

    private final HistoryService personService;
    private final HistoryService genreHistory;
    private final HistoryService bookHistory;
    private final HistoryService authorHistory;

    @Autowired
    public HistoryRestController(@Qualifier("personHistoryService") HistoryService personService,
                                 @Qualifier("genreHistoryService") HistoryService genreHistory,
                                 @Qualifier("bookHistoryService") HistoryService bookHistory,
                                 @Qualifier("authorHistoryService") HistoryService authorHistory) {
        this.personService = personService;
        this.genreHistory = genreHistory;
        this.bookHistory = bookHistory;
        this.authorHistory = authorHistory;
    }

    @Operation(description = "Показать историю операций над пользователями")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HistoryDto.class)))})
    })
    @GetMapping("/persons")
    public ResponseEntity<List<HistoryDto>> getPersonHistory(
            @RequestParam(name = "entityId")
            @Parameter(description = "Id сущности")
            @Positive Long entityId)
           {
        return ResponseEntity.ok(personService.getHistoryEntity(entityId));
    }

    @Operation(description = "Показать историю операций над жанрами")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HistoryDto.class)))})
    })
    @GetMapping("/genres")
    public ResponseEntity<List<HistoryDto>> getGenreHistory(
            @RequestParam(name = "entityId")
            @Parameter(description = "Id сущности")
            @Positive Long entityId)
    {
        return ResponseEntity.ok(genreHistory.getHistoryEntity(entityId));
    }

    @Operation(description = "Показать историю операций над авторами")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HistoryDto.class)))})
    })
    @GetMapping("/authors")
    public ResponseEntity<List<HistoryDto>> getAuthorHistory(
            @RequestParam(name = "entityId")
            @Parameter(description = "Id сущности")
            @Positive Long entityId)
    {
        return ResponseEntity.ok(authorHistory.getHistoryEntity(entityId));
    }

    @Operation(description = "Показать историю операций над книгами")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HistoryDto.class)))})
    })
    @GetMapping("/books")
    public ResponseEntity<List<HistoryDto>> getBookHistory(
            @RequestParam(name = "entityId")
            @Parameter(description = "Id сущности")
            @Positive Long entityId)
    {
        return ResponseEntity.ok(bookHistory.getHistoryEntity(entityId));
    }
}
