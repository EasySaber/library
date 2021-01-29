package com.example.feignclient.dto.author;

import com.example.feignclient.dto.book.BookDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.ZonedDateTime;
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
public class AuthorDto {

    private Long id;

    @NotBlank(message = "Пустое значение")
    @Size(max = 64)
    private String firstName;

    @Size(max = 64)
    private String middleName;

    @NotBlank(message = "Пустое значение")
    @Size(max = 64)
    private String lastName;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<BookDto> books;

    private LocalDate dateTimeOfBirth;

    private ZonedDateTime dateTimeCreated;
}
