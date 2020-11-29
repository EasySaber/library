package com.example.sshomework.dto.author;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * @author Aleksey Romodin
 */
@Builder
@Getter
public class AuthorSearchRequest {
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final LocalDate starDateCreated;
    private final LocalDate endDateCreated;
}
