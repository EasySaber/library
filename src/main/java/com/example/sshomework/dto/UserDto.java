package com.example.sshomework.dto;

import com.example.sshomework.dto.view.*;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.validation.constraints.*;

/**
 * @author Aleksey Romodin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @JsonView(View.All.class)
    private Long id;

    @NotBlank(message = "Пустое значение")
    @Size(min = 2, max = 64, message = "Имя должно содержать от 2 до 64 символов")
    @JsonView(View.Public.class)
    private String username;

    @NotBlank(message = "Пустое значение")
    @Size(min = 6, max = 64, message = "Пароль должен содержать от 6 до 64 символов")
    @JsonView(View.Public.class)
    private String password;
}
