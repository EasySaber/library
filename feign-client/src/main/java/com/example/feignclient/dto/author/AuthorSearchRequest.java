package com.example.feignclient.dto.author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author Aleksey Romodin
 */
@Getter
@AllArgsConstructor
public class AuthorSearchRequest {
    private final String firstName;
    private final String middleName;
    private final String lastName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate starDateCreated;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate endDateCreated;
}
