package com.example.sshomework.api;

import com.example.sshomework.dto.Person;
import com.example.sshomework.service.person.PersonService;
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
@RequestMapping("/api/persons")
public class PersonRestController {

    private final PersonService personService;

    @Operation(description = "Показать всех")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Person.class))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllPersons() {
        return personService.getAll().isEmpty() ?
                ResponseEntity.notFound().build() : ResponseEntity.ok(personService.getAll());
    }

    @Operation(description = "Поиск человека по имени")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск успешен",
                    content = {@Content(mediaType = "application/json",
                            schema =  @Schema(implementation = Person.class))}),
            @ApiResponse(responseCode = "404", description = "Поиск не дал результатов", content = @Content)
    })
    @GetMapping("/findByFirstName")
    public ResponseEntity<?> findByFirstName(@RequestParam(name = "firstName")
                                                 @Parameter(description = "Имя") String firstName) {
        return personService.findByFirstName(firstName).isEmpty() ?
                ResponseEntity.notFound().build() : ResponseEntity.ok(personService.findByFirstName(firstName));
    }

    @Operation(description = "Добавление нового человека")
    @ApiResponse(responseCode = "200", description = "Данные добавлены",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Person.class))})

    @PostMapping("/add")
    public ResponseEntity<?> addNewPerson(@RequestParam(name = "firstName")
                                              @Parameter(description = "Имя") String firstName,
                                          @RequestParam(name = "middleName")
                                          @Parameter(description = "Отчество") String middleName,
                                          @RequestParam(name = "lastName")
                                              @Parameter(description = "Фамилия") String lastName,
                                          @RequestParam(name = "age")
                                          @Parameter(description = "Возраст") Integer age) {
        personService.addNewPerson(new Person(firstName, middleName, lastName, age));
        return ResponseEntity.ok(personService.getAll());
    }

    @Operation(description = "Удаление человека по ФИО")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные удалены", content = @Content),
            @ApiResponse(responseCode = "404", description = "Данные не найдены", content = @Content)
    })
    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePerson(@RequestParam(name = "firstName")
                                              @Parameter(description = "Имя") String firstName,
                                          @RequestParam(name = "middleName")
                                          @Parameter(description = "Отчество") String middleName,
                                          @RequestParam(name = "lastName")
                                              @Parameter(description = "Фамилия") String lastName) {
        String FIO = firstName + " " + middleName + " " + lastName;
        if (personService.findByFIO(FIO).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            personService.deletePerson(FIO);
            return ResponseEntity.ok().build();
        }

    }
}
