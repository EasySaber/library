package com.example.sshomework.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * @author Aleksey Romodin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @JsonView(View.Public.class)
    @NotBlank(message = "Пустое значение")
    @Size(max = 64)
    private String firsName;
    @JsonView(View.Public.class)
    @NotBlank(message = "Пустое значение")
    @Size(max = 64)
    private String middleName;
    @JsonView(View.Public.class)
    @NotBlank(message = "Пустое значение")
    @Size(max = 64)
    private String lastName;
    @JsonView(View.Public.class)
    @NotNull(message = "Пустое значение")
    @Schema(pattern = "yyyy-MM-dd", example = "2020-02-20")
    private LocalDate dateOfBirth;

    @JsonView(View.Private.class)
    public String getFullName() {
        return firsName + " " + middleName + " " + lastName;
    }
}

