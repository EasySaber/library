package com.example.sshomework.api;

import com.example.sshomework.aspect.annotation.LoggerExecuteTime;
import com.example.sshomework.dto.author.AuthorDto;
import com.example.sshomework.dto.author.AuthorSearchRequest;
import com.example.sshomework.dto.view.View;
import com.example.sshomework.entity.Author;
import com.example.sshomework.exception.DeleteRelatedDataException;
import com.example.sshomework.service.author.AuthorService;
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

import javax.validation.Valid;
import java.util.List;

/**
 * @author Aleksey Romodin
 */
@LoggerExecuteTime
@RestController
@RequestMapping(value = "api/author")
@Tag(name = "Author", description = "Author API")
public class AuthorRestController {

    private final AuthorService authorService;
    @Autowired
    public AuthorRestController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @JsonView(View.Private.class)
    @Operation(description = "Поиск автора по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class))}),
            @ApiResponse(responseCode = "404", description = "Автор не найден", content = @Content)
    })
    @GetMapping("/getById")
    public ResponseEntity<AuthorDto> getAuthorById(
            @RequestParam(name = "id")
            @Parameter(description = "Id автора") Long id)
    {
        return ResponseEntity.of(authorService.getAuthorById(id));

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @JsonView(View.Public.class)
    @Operation(description = "Показать всех авторов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AuthorDto.class)))})
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<AuthorDto>> getAll() {
        return ResponseEntity.ok(authorService.getAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @JsonView(View.Public.class)
    @Operation(description = "Показать авторов и их книги.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AuthorDto.class)))})
    })
    @GetMapping("/getBooksByAuthor")
    public ResponseEntity<List<AuthorDto>> getBooksByAuthor() {
        return ResponseEntity.ok(authorService.getAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(View.AuthorOfAllTheBook.class)
    @Operation(description = "Добавление нового автора.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись добавлена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class))}),
            @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных", content = @Content)
    })
    @PostMapping("/add")
    public ResponseEntity<AuthorDto> addNewAuthor(
            @JsonView(View.AddAuthor.class)
            @Parameter(description = "Новый автор")
            @Valid @RequestBody AuthorDto authorDto)
    {
        return ResponseEntity.of(authorService.addNewAuthor(authorDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Удаление автора по id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись удалена", content = @Content),
            @ApiResponse(responseCode = "404", description = "Запись не найдена", content = @Content),
            @ApiResponse(responseCode = "423",
                    description = "Невозможно удалить запись: одна из книг автора находится у читателя.",
                    content = @Content)
    })
    @DeleteMapping("/delete")
    public void deleteAuthorById(
            @RequestParam(name = "id")
            @Parameter(description = "Id автора") Long id)
            throws DeleteRelatedDataException
    {
        authorService.deleteAuthorById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @JsonView(View.Private.class)
    @Operation(description = "Поиск автора по параметрам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск выполнен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Author.class)))}),
            @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных")
    })
    @GetMapping("/getAuthorInParameter")
    public ResponseEntity<List<AuthorDto>> getAuthorInParameter(
            @Parameter(description = "Параметры") AuthorSearchRequest request)
    {
        return ResponseEntity.ok(authorService.getAuthorInParameters(request));
    }

}
