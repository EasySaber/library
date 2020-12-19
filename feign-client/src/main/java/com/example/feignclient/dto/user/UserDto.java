package com.example.feignclient.dto.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author Aleksey Romodin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDto {

    private Long id;

    @NotBlank(message = "Пустое значение")
    @Size(min = 2, max = 64, message = "Имя должно содержать от 2 до 64 символов")
    private String username;

    @ToString.Exclude
    @NotBlank(message = "Пустое значение")
    @Size(min = 6, max = 64, message = "Пароль должен содержать от 6 до 64 символов")
    private String password;

    @NotNull(message = "Пустое значение")
    private RolesUser role;

    @EqualsAndHashCode.Exclude
    private LocalDateTime dateTimeInput = LocalDateTime.now();

    @EqualsAndHashCode.Exclude
    private Integer loginAttemptInput = 0;
}
