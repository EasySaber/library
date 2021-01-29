package com.example.sshomework.dto.author;

import com.example.sshomework.dto.book.BookDto;
import com.example.sshomework.dto.view.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

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

    @JsonView({View.Public.class, View.Private.class, View.AuthorOfAllTheBook.class,
            View.PersonOfAllTheBook.class, View.Book.class})
    private Long id;

    @NotBlank(message = "Пустое значение")
    @Size(max = 64)
    @JsonView({View.Public.class, View.Private.class, View.AddAuthor.class,
            View.AuthorOfAllTheBook.class, View.PersonOfAllTheBook.class, View.BookPost.class})
    private String firstName;

    @Size(max = 64)
    @JsonView({View.Public.class, View.Private.class, View.AddAuthor.class,
            View.AuthorOfAllTheBook.class, View.PersonOfAllTheBook.class, View.BookPost.class})
    private String middleName;

    @NotBlank(message = "Пустое значение")
    @Size(max = 64)
    @JsonView({View.Public.class, View.Private.class, View.AddAuthor.class,
            View.AuthorOfAllTheBook.class, View.PersonOfAllTheBook.class, View.BookPost.class})
    private String lastName;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonView({View.Public.class, View.AddAuthor.class})
    private Set<BookDto> books;

    @JsonView({View.Public.class, View.Private.class, View.AddAuthor.class,
            View.AuthorOfAllTheBook.class, View.PersonOfAllTheBook.class, View.BookPost.class})
    private LocalDate dateTimeOfBirth;

    @JsonView({View.Public.class, View.Private.class, View.AddAuthor.class,
            View.AuthorOfAllTheBook.class, View.PersonOfAllTheBook.class, View.BookPost.class})
    private ZonedDateTime dateTimeCreated;
}
