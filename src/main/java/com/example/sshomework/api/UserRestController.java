package com.example.sshomework.api;

import com.example.sshomework.aspect.annotation.LoggerExecuteTime;
import com.example.sshomework.dto.user.UserDto;
import com.example.sshomework.dto.view.View;
import com.example.sshomework.exception.NotUniqueValueException;
import com.example.sshomework.service.user.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Aleksey Romodin
 */
@LoggerExecuteTime
@RestController
@RequestMapping(value = "api/user")
@Tag(name = "User", description = "User API")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(View.Public.class)
    @Operation(description = "Добавление нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись добавлена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных", content = @Content)
    })
    @PostMapping("/add")
    public void addNewUser(
            @Parameter(description = "Новый пользователь")
            @Valid @RequestBody UserDto user) throws NotUniqueValueException {
        userService.addNewUser(user);
    }

}
