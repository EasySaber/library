package com.example.sshomework.dto;

import com.example.sshomework.dto.genre.GenreDto;
import com.example.sshomework.dto.view.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author Aleksey Romodin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    @JsonView({View.All.class, View.PersonOfAllTheBookSmall.class, View.BookPost.class,
            View.UpdateBookGenres.class, View.LibraryCard.class})
    private Long id;

    @JsonView({View.Public.class, View.AddAuthor.class, View.AuthorOfAllTheBook.class,
            View.PersonOfAllTheBookSmall.class, View.Book.class, View.LibraryCard.class})
    @NotBlank(message = "Пустое значение")
    @Size(max = 200)
    private String bookName;

    @JsonView({View.All.class, View.PersonOfAllTheBook.class, View.Book.class})
    private AuthorDto authorBookDto;

    @Valid
    @JsonView({View.Public.class, View.AddAuthor.class, View.PersonOfAllTheBook.class,
            View.Book.class, View.UpdateBookGenres.class})
    private Set<GenreDto> genres;

    @JsonView(View.All.class)
    private Set<LibraryCardDto> personsDto;
}
