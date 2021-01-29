package com.example.feignclient.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Positive;

/**
 * @author Aleksey Romodin
 */
@Getter
@AllArgsConstructor
@ToString
public class BookSearchRequest {
    private final String genre;
    @Positive(message = "Значение должно быть больше 0")
    private final Integer yearPublication;
    private final Sings sing;
}

