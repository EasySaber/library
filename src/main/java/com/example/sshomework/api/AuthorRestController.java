package com.example.sshomework.api;

import com.example.sshomework.dto.author.AuthorDto;
import com.example.sshomework.dto.author.AuthorSearchRequest;
import com.example.sshomework.dto.view.View;
import com.example.sshomework.entity.Author;
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
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author Aleksey Romodin
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/author")
@Tag(name = "Author", description = "Author API")
public class AuthorRestController {

    private final AuthorService authorService;

    @JsonView(View.Private.class)
    @Operation(description = "Поиск автора по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/getById")
    public ResponseEntity<?> getAuthorById(@RequestParam(name = "id")
                                           @Parameter(description = "Id автора") Long id) {
        Optional<AuthorDto> author = authorService.getAuthorById(id);
        return !author.isPresent() ? ResponseEntity.notFound().build() : ResponseEntity.ok(author.get());

    }

    @JsonView(View.Public.class)
    @Operation(description = "Показать всех авторов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AuthorDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        List<AuthorDto> authorDtoList = authorService.getAll();
        return authorDtoList.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(authorDtoList);
    }

    @JsonView(View.Public.class)
    @Operation(description = "Показать список книг автора: автор, книги, жанры.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AuthorDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/getBooksByAuthor")
    public ResponseEntity<?> getBooksByAuthor() {
        List<AuthorDto> authorDtoList = authorService.getAll();
        return authorDtoList.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(authorDtoList);
    }

    @JsonView(View.AuthorOfAllTheBook.class)
    @Operation(description = "Добавление нового автора.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись добавлена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class))}),
            @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных", content = @Content)
    })
    @PostMapping("/add")
    public ResponseEntity<?> addNewAuthor(@JsonView(View.AddAuthor.class)
                                          @Parameter(description = "Новый автор")
                                          @Valid @RequestBody AuthorDto authorDto) {
        return ResponseEntity.ok(authorService.addNewAuthor(authorDto));
    }

    @Operation(description = "Удаление автора по id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись удалена", content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Невозможно удалить запись: одна из книг автора находится у читателя.",
                    content = @Content)
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAuthorById(@RequestParam(name = "id")
                                                 @Parameter(description = "Id автора") Long id) {
        return authorService.deleteAuthorById(id) ?
                ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }


    @JsonView(View.Private.class)
    @Operation(description = "Поиск автора по параметрам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск выполнен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Author.class)))}),
            @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных"),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/getAuthorInParameter")
    public ResponseEntity<?> getAuthorInParameter(
            @Parameter(description = "Имя автора")
            @RequestParam(name = "firstName", required = false) String firstName,
            @Parameter(description = "Отчество автора")
            @RequestParam(name = "middleName", required = false) String middleName,
            @Parameter(description = "Фамилия автора")
            @RequestParam(name = "lastName", required = false) String lastName,
            @Parameter(description = "Стартовая дата добавления")
            @RequestParam(name = "starDateCreated", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") @Valid LocalDate starDateCreated,
            @Parameter(description = "Конечная дата добавления")
            @RequestParam(name = "endDateCreated", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") @Valid LocalDate endDateCreated) {
        AuthorSearchRequest request = AuthorSearchRequest.builder()
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .starDateCreated(starDateCreated)
                .endDateCreated(endDateCreated).build();
        List<AuthorDto> authors = authorService.getAuthorInParameters(request);
        return authors.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(authors);
    }

}
