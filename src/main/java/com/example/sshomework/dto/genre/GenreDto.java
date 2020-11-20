package com.example.sshomework.dto.genre;

import com.example.sshomework.dto.view.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Aleksey Romodin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {

    @JsonView({View.All.class, View.AddAuthor.class, View.PersonOfAllTheBook.class, View.Book.class,
            View.UpdateBookGenres.class})
    private Long id;

    @NotBlank(message = "Пустое значение")
    @Size(max = 64)
    @JsonView({View.Public.class, View.PersonOfAllTheBook.class, View.BookPost.class})
    private String genreName;
}
