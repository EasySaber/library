package com.example.sshomework.dto.book;

import lombok.*;

/**
 * @author Aleksey Romodin
 */
@Getter
@Builder
public class BookSearchRequest {
    private final String genre;
    private final Integer yearPublication;
    private final Sings sing;
}

