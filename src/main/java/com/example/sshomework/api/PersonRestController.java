package com.example.sshomework.api;

import com.example.sshomework.dto.PersonDto;
import com.example.sshomework.dto.view.View;
import com.example.sshomework.service.person.PersonService;
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

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Aleksey Romodin
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/persons")
@Tag(name = "Person", description = "Person API")
public class PersonRestController {

    private final PersonService personService;

    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(View.All.class)
    @Operation(description = "Показать всех(без книг).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PersonDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllPersons() {
        List<PersonDto> persons = personService.getAll();
        return persons.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(persons);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(View.PersonOfAllTheBook.class)
    @Operation(description = "Список, взятых пользователем, книг. Поиск по id пользователя. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PersonDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/getBooksByAuthorId")
    public ResponseEntity<?> getBooksByAuthorId(@RequestParam(name = "id")
                                                @Parameter(description = "id пользователя") Long id) {
        PersonDto person = personService.getBooksByAuthorId(id);
        return person != null ? ResponseEntity.ok(person) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(View.All.class)
    @Operation(description = "Добавление нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные добавлены",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PersonDto.class)))}),
            @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных", content = @Content),
    })
    @PostMapping("/add")
    public ResponseEntity<PersonDto> addNewPerson(@JsonView(View.Public.class)
                                                  @Parameter(description = "Добавление данных нового пользователя")
                                                  @Valid @RequestBody PersonDto personDto) {
        return ResponseEntity.ok(personService.addNewPerson(personDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Удаление данных человека по ФИО")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные удалены", content = @Content),
            @ApiResponse(responseCode = "404", description = "Данные не найдены", content = @Content)
    })
    @DeleteMapping("/deleteByFullName")
    public ResponseEntity<Void> deletePersonByFullName(@RequestParam(name = "firstName")
                                                       @Parameter(description = "Имя") String firstName,
                                                       @RequestParam(name = "middleName")
                                                       @Parameter(description = "Отчество") String middleName,
                                                       @RequestParam(name = "lastName")
                                                       @Parameter(description = "Фамилия") String lastName) {
        return personService.deletePersonsByFullName(firstName, middleName, lastName) ?
                ResponseEntity.ok().build() : ResponseEntity.notFound().build();

    }

    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(View.All.class)
    @Operation(description = "Обновление данных пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные добавлены",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PersonDto.class)))}),
            @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных", content = @Content),
    })
    @PutMapping("/update")
    public ResponseEntity<?> updatePerson(@Parameter(description = "Обновление данных пользователя")
                                          @Valid @RequestBody PersonDto personDto) {
        Optional<PersonDto> person = personService.updatePerson(personDto);
        return person.isPresent() ? ResponseEntity.ok(person) : ResponseEntity.badRequest().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Удаление данных пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные удалены", content = @Content),
            @ApiResponse(responseCode = "404", description = "Данные не найдены", content = @Content)
    })
    @DeleteMapping("/deleteById")
    public ResponseEntity<Void> deletePersonById(@RequestParam(name = "id")
                                                 @Parameter(description = "Id пользовалетя") Long id) {
        return personService.deletePersonById(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(View.PersonOfAllTheBookSmall.class)
    @Operation(description = "Добавление новой записи в картотеке")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные добавлены",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PersonDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Id пользователя/книги не найдены", content = @Content),
    })
    @PostMapping("/addNewPostLibraryCard")
    public ResponseEntity<PersonDto> addNewPostLibraryCard(@Parameter(description = "Id пользователя")
                                                           @RequestParam(name = "personId") Long personId,
                                                           @Parameter(description = "Id книги")
                                                           @RequestParam(name = "bookId") Long bookId)  {
        PersonDto person = personService.addNewPostLibraryCard(personId, bookId);
        return person != null ? ResponseEntity.ok(person) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(View.PersonOfAllTheBookSmall.class)
    @Operation(description = "Удаление записи из картотеки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные удалены",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PersonDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Id пользователя/книги не найдены", content = @Content),
    })
    @DeleteMapping("/deletePostLibraryCard")
    public ResponseEntity<PersonDto> deletePostLibraryCard(@Parameter(description = "Id пользователя")
                                                           @RequestParam(name = "personId") Long personId,
                                                           @Parameter(description = "Id книги")
                                                           @RequestParam(name = "bookId") Long bookId) {
        PersonDto person = personService.deletePostLibraryCard(personId, bookId);
        return person != null ? ResponseEntity.ok(person) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('USER')")
    @JsonView(View.PersonOfAllTheBook.class)
    @Operation(description = "Список, взятых авторизированнымм пользователем, книг.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PersonDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/getListUserBooks")
    public ResponseEntity<?> getListUserBooks() {
        PersonDto person = personService.getListUserBooks();
        return person != null ? ResponseEntity.ok(person) : ResponseEntity.notFound().build();
    }
}
