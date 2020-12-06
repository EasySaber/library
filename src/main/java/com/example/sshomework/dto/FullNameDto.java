package com.example.sshomework.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Aleksey Romodin
 */
@Getter
@AllArgsConstructor
public class FullNameDto {
    private final String firstName;
    private final String middleName;
    private final String lastName;
}
