package com.example.feignclient.dto;

import com.example.feignclient.dto.book.BookDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Period;
import java.time.ZonedDateTime;

/**
 * @author Aleksey Romodin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class LibraryCardDto {

    private Long id;

    private BookDto bookDto;

    private PersonDto personDto;

    @EqualsAndHashCode.Exclude
    private ZonedDateTime dateTimeCreated;

    @EqualsAndHashCode.Exclude
    private ZonedDateTime dateTimeReturn;

    private Long daysDept() {
        long daysDept = Period.between(dateTimeReturn.toLocalDate(),
                ZonedDateTime.now().toLocalDate()).getDays();
        return daysDept < 0 ? 0 : daysDept;
    }

}
