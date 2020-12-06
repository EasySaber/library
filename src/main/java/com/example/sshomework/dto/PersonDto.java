package com.example.sshomework.dto;

import com.example.sshomework.dto.user.UserDto;
import com.example.sshomework.dto.view.View;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class PersonDto {

    @JsonView({View.All.class, View.PersonOfAllTheBookSmall.class,  View.LibraryCard.class})
    private Long id;

    @JsonView({View.Public.class, View.PersonOfAllTheBookSmall.class})
    @NotBlank(message = "Пустое значение")
    @Size(max = 64)
    private String firstName;

    @JsonView({View.Public.class, View.PersonOfAllTheBookSmall.class})
    @NotBlank(message = "Пустое значение")
    @Size(max = 64)
    private String middleName;

    @JsonView({View.Public.class, View.PersonOfAllTheBookSmall.class})
    @NotBlank(message = "Пустое значение")
    @Size(max = 64)
    private String lastName;

    @JsonView({View.Public.class, View.PersonOfAllTheBookSmall.class})
    @NotNull(message = "Пустое значение")
    @Schema(pattern = "yyyy-MM-dd", example = "2020-02-20")
    private LocalDate dateOfBirth;

    @JsonView({View.PersonOfAllTheBookSmall.class})
    private Set<LibraryCardDto> booksDto;

    @JsonView(View.Private.class)
    private UserDto account;

    @JsonView({View.Private.class, View.LibraryCard.class})
    public String getFullName() {
        return firstName + " " + middleName + " " + lastName;
    }
}

