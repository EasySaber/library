package com.example.sshomework.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author Aleksey Romodin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookStatusDto {
    private Long id;
    private String bookName;
    private String authorBook;
    private String genreList;
    private LocalDate DatePublication;
    private String status;
}
