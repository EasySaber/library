package com.example.sshomework.dto;

import com.example.sshomework.dto.book.BookDto;
import com.example.sshomework.dto.view.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Period;
import java.time.ZonedDateTime;

/**
 * @author Aleksey Romodin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LibraryCardDto {

    @JsonView({View.Private.class, View.LibraryCard.class})
    private Long id;

    @JsonView({View.PersonOfAllTheBookSmall.class, View.LibraryCard.class})
    private BookDto bookDto;

    @JsonView({View.Private.class, View.LibraryCard.class})
    private PersonDto personDto;

    @JsonView({View.PersonOfAllTheBook.class, View.LibraryCard.class})
    private ZonedDateTime dateTimeCreated;

    @JsonView({View.PersonOfAllTheBook.class, View.LibraryCard.class})
    private ZonedDateTime dateTimeReturn;

    @JsonView(View.LibraryCard.class)
    private Long daysDept() {
        long daysDept = Period.between(dateTimeReturn.toLocalDate(),
                ZonedDateTime.now().toLocalDate()).getDays();
        return daysDept < 0 ? 0 : daysDept;
    }

}
