package com.example.feignclient.dto.book;

import lombok.*;

import java.time.LocalDate;

/**
 * @author Aleksey Romodin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BookStatusDto {
    private Long id;
    private String bookName;
    private String authorBook;
    private String genreList;
    private LocalDate DatePublication;
    private String status;
}
