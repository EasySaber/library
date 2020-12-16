package com.example.sshomework.api;

import com.example.sshomework.aspect.annotation.LoggerExecuteTime;
import com.example.sshomework.dto.FullNameDto;
import com.example.sshomework.dto.book.BookDto;
import com.example.sshomework.dto.book.BookSearchRequest;
import com.example.sshomework.dto.view.View;
import com.example.sshomework.exception.DeleteRelatedDataException;
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
@RequestMapping(value = "api/books")
@Tag(name = "Book", description = "Book API")
public class BookRestController {

    private final BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @JsonView(View.BookPost.class)
    @Operation(description = "Список книг и фильтром по автору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BookDto.class)))})
    })
    @GetMapping("/getByAuthorFilter")
    public ResponseEntity<List<BookDto>> getByAuthorFilter(
            @Parameter(description = "Параметры поиска") FullNameDto request)
    {
        return ResponseEntity.ok(bookService.getByAuthorFilter(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @JsonView(View.BookPost.class)
    @Operation(description = "Поиск по жанру")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BookDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Жанр не найден.", content = @Content)
    })
    @GetMapping("/getByGenre")
    public ResponseEntity<List<BookDto>> getByGenre(
            @RequestParam(name = "genreName")
            @Parameter(description = "Название жанра") String genreName)
    {
        return ResponseEntity.ok(bookService.getByGenre(genreName));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(View.BookPost.class)
    @Operation(description = "Добавление новой книги")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные добавлены",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class))}),
            @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных", content = @Content)
    })
    @PostMapping("/add")
    public ResponseEntity<BookDto> addNewBook(
            @JsonView(View.Book.class)
            @Parameter(description = "Добавление данных о новой книге")
            @Valid @RequestBody BookDto bookDto)
    {
        return ResponseEntity.of(bookService.addNewBook(bookDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Удаление книги по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные удалены", content = @Content),
            @ApiResponse(responseCode = "404", description = "Данные не найдены", content = @Content),
            @ApiResponse(responseCode = "423", description = "Запрет на удаление", content = @Content)
    })
    @DeleteMapping("/delete")
    public void deleteBook(
            @RequestParam(name = "id")
            @Parameter(description = "Id книги") Long id)
            throws DeleteRelatedDataException
    {
        bookService.deleteBookById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(View.BookPost.class)
    @Operation(description = "Обновление жанров книги")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные обновлены",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class))}),
            @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных", content = @Content),

    })
    @PutMapping("/updateGenres")
    public ResponseEntity<BookDto> updateGenres(
            @Parameter(description = "Id книги")
            @RequestParam(name = "bookId") Long bookId,
            @Parameter(description = "Id жанров книги")
            @RequestParam(name = "genres[]") List<String> genres)
    {
        return ResponseEntity.of(bookService.updateGenres(bookId, genres));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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
    public ResponseEntity<List<BookDto>> getBookInParameter(
            @Parameter(description = "Параметры")
            @Valid BookSearchRequest request)
    {
        if ((request.getYearPublication() != null) && (request.getSing() == null)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bookService.getBookInParameters(request));
    }

}
