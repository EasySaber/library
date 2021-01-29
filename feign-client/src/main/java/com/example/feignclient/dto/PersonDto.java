package com.example.feignclient.dto;

import com.example.feignclient.dto.user.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

/**
 * @author Aleksey Romodin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PersonDto {

    private Long id;

    @NotBlank(message = "Пустое значение")
    @Size(max = 64)
    private String firstName;

    @NotBlank(message = "Пустое значение")
    @Size(max = 64)
    private String middleName;

    @NotBlank(message = "Пустое значение")
    @Size(max = 64)
    private String lastName;

    @NotNull(message = "Пустое значение")
    @Schema(pattern = "yyyy-MM-dd", example = "2020-02-20")
    private LocalDate dateOfBirth;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<LibraryCardDto> booksDto;

    private UserDto account;

    public String getFullName() {
        return firstName + " " + middleName + " " + lastName;
    }
}

