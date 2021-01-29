package com.example.feignclient.dto.book;

import com.example.feignclient.dto.LibraryCardDto;
import com.example.feignclient.dto.author.AuthorDto;
import com.example.feignclient.dto.genre.GenreDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
public class BookDto {

    private Long id;

    @NotBlank(message = "Пустое значение")
    @Size(max = 200)
    private String bookName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate datePublication;

    private AuthorDto authorBookDto;

    @Valid
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<GenreDto> genres;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<LibraryCardDto> personsDto;
}
