package com.example.sshomework.api;

import com.example.sshomework.dto.Book;
import com.example.sshomework.service.book.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aleksey Romodin
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/books")
public class BookRestController {

    private final BookService bookService;

    @Operation(description = "Показать все")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        return bookService.getAll().isEmpty() ?
                ResponseEntity.notFound().build() : ResponseEntity.ok(bookService.getAll());

    }

    @Operation(description = "Поиск по автору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/findByAuthor")
    public ResponseEntity<?> findByAuthor(@RequestParam(name = "author")
                                          @Parameter(description = "ФИО автора") String author) {
        return bookService.findByAuthor(author).isEmpty() ?
                ResponseEntity.notFound().build() : ResponseEntity.ok(bookService.findByAuthor(author));
    }

    @Operation(description = "Добаление новой книги")
    @ApiResponse(responseCode = "200", description = "Данные добавлены",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class))})
    @PostMapping("/add")
    public ResponseEntity<?> addNewBook(@RequestParam(name = "name")
                                        @Parameter(description = "Название книги") String name,
                                        @RequestParam(name = "author")
                                        @Parameter(description = "Автор") String author,
                                        @RequestParam(name = "genre")
                                            @Parameter(description = "Жанр") String genre) {
        bookService.addNewBook(new Book(name, author, genre));
        return ResponseEntity.ok(bookService.getAll());
    }

    @Operation(description = "Удаление книги по автору и названию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные удалены",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "404", description = "Данные не найдены", content = @Content)
    })
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteBook(@RequestParam(name = "author")
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
