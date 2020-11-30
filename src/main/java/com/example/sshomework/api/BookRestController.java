package com.example.sshomework.api;

import com.example.sshomework.dto.book.*;
import com.example.sshomework.dto.view.*;
import com.example.sshomework.service.book.*;
import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.*;

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
        List<BookDto> books = bookService.getByAuthorFilter(firstName, middleName, lastName);
        return books.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(books);
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
        List<BookDto> books = bookService.getByGenre(id);
        if (books != null) {
            return books.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(books);
        }
        return ResponseEntity.notFound().build();
    }


    @JsonView(View.BookPost.class)
    @Operation(description = "Добавление новой книги")
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
        BookDto book = bookService.addNewBook(bookDto);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.badRequest().build();
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
        return bookService.deleteBookById(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
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
        BookDto book = bookService.updateGenres(bookId, genres);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
    }

    @JsonView(View.BookPost.class)
    @Operation(description = "Поиск книг по параметрам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BookDto.class)))}),
            @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных"),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/getBookInParameter")
    public ResponseEntity<?> getBookInParameter(
            @Parameter(description = "Жанр")
            @RequestParam(name = "genre", required = false) String genre,
            @Parameter(description = "Год издания")
            @RequestParam(name = "year", required = false) @Valid Integer yearPublication,
            @Parameter(description = "Признак фильтра('>', '<', '=')")
            @RequestParam(name = "sing", required = false) @Valid Sings sing)
    {

        if ((yearPublication != null) & (sing == null)) {
            return ResponseEntity.badRequest().build();
        }
        BookSearchRequest request = BookSearchRequest.builder()
                .genre(genre)
                .yearPublication(yearPublication)
                .sing(sing)
                .build();
        List<BookDto> books = bookService.getBookInParameters(request);
        return books.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(books);
    }

}
