package com.example.sshomework.api;

import com.example.sshomework.dto.Book;
import com.example.sshomework.dto.View;
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

import javax.validation.Valid;
import java.util.List;

/**
 * @author Aleksey Romodin
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/books")
@Tag(name = "Book", description = "Book API")
public class BookRestController {

    private final BookService bookService;

    @JsonView(View.All.class)
    @Operation(description = "Показать все")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Book.class)))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        return bookService.getAll().isEmpty() ?
                ResponseEntity.notFound().build() : ResponseEntity.ok(bookService.getAll());

    }

    @JsonView(View.All.class)
    @Operation(description = "Поиск по автору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Book.class)))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/findByAuthor")
    public ResponseEntity<?> findByAuthor(@RequestParam(name = "author")
                                              @Parameter(description = "ФИО автора") String author) {
        return bookService.findByAuthor(author).isEmpty() ?
                ResponseEntity.notFound().build() : ResponseEntity.ok(bookService.findByAuthor(author));
    }

    @JsonView(View.Public.class)
    @Operation(description = "Добаление новой книги")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные добавлены",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Book.class)))}),
            @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных", content = @Content),

    })
    @PostMapping("/add")
    public ResponseEntity<List<Book>> addNewBook(@JsonView(View.All.class)
                                                     @Parameter(description = "Добавление данных о новой книге", required = true)
                                                     @Valid @RequestBody Book book) {

        bookService.addNewBook(book);
        return ResponseEntity.ok(bookService.getAll());
    }

    @Operation(description = "Удаление книги по автору и названию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные удалены", content = @Content),
            @ApiResponse(responseCode = "404", description = "Данные не найдены", content = @Content)
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteBook(@RequestParam(name = "author")
                                               @Parameter(description = "Автор") String author,
                                           @RequestParam(name = "name")
                                           @Parameter(description = "Название книги") String name) {
        if (bookService.findByAuthorByName(author, name).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            bookService.deleteBook(author, name);
            return ResponseEntity.ok().build();
        }
    }

}
