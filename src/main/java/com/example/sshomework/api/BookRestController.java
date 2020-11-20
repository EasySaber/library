package com.example.sshomework.api;

import com.example.sshomework.dto.BookDto;
import com.example.sshomework.dto.view.View;
import com.example.sshomework.service.book.BookService;
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

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/books")
@Tag(name = "Book", description = "Book API")
public class BookRestController {

    private final BookService bookService;

    @JsonView(View.BookPost.class)
    @Operation(description = "Список книг и фильтром по автору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BookDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/getByAuthorFilter")
    public ResponseEntity<?> getByAuthorFilter(@Parameter(description = "Имя автора")
                                                   @RequestParam(name = "firstName", required = false) String firstName,
                                               @Parameter(description = "Отчество автора")
                                               @RequestParam(name = "middleName", required = false) String middleName,
                                               @Parameter(description = "Фамилия автора")
                                                   @RequestParam(name = "lastName", required = false) String lastName)
    {
        return bookService.getByAuthorFilter(firstName, middleName, lastName).isEmpty() ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(bookService.getByAuthorFilter(firstName, middleName, lastName));
    }

    @JsonView(View.BookPost.class)
    @Operation(description = "Поиск по жанру")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BookDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/getByGenre")
    public ResponseEntity<?> getByGenre(@RequestParam(name = "id")
                                            @Parameter(description = "Id жанра") Long id) {
        if (bookService.getByGenre(id) != null) {
            return bookService.getByGenre(id).isEmpty() ?
                    ResponseEntity.notFound().build() : ResponseEntity.ok(bookService.getByGenre(id));
        }
        return ResponseEntity.notFound().build();
    }


    @JsonView(View.BookPost.class)
    @Operation(description = "Добаление новой книги")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные добавлены",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class))}),
            @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных", content = @Content),

    })
    @PostMapping("/add")
    public ResponseEntity<?> addNewBook(@JsonView(View.Book.class)
                                            @Parameter(description = "Добавление данных о новой книге")
                                            @RequestBody BookDto bookDto) {
        return bookService.addNewBook(bookDto) != null ?
                ResponseEntity.ok(bookService.addNewBook(bookDto)) : ResponseEntity.badRequest().build();
    }

    @Operation(description = "Удаление книги по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные удалены", content = @Content),
            @ApiResponse(responseCode = "404", description = "Данные не найдены/книга в пользовании",
                    content = @Content)
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteBook(@RequestParam(name = "id")
                                               @Parameter(description = "Id книги") Long id) {
        return bookService.deleteBookById(id) ?
                ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @JsonView(View.BookPost.class)
    @Operation(description = "Обновление жанров книги")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные обновлены",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class))}),
            @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных", content = @Content),

    })
    @PutMapping("/updateGenres")
    public ResponseEntity<?> updateGenres(@Parameter(description = "Id книги")
                                        @RequestParam(name = "bookId") Long bookId,
                                          @Parameter(description = "Id жанров книги")
                                          @RequestParam(name = "genres[]") List<Long> genres) {
        return bookService.updateGenres(bookId, genres) != null ?
                ResponseEntity.ok(bookService.updateGenres(bookId, genres)) : ResponseEntity.notFound().build();
    }

}
