package com.example.sshomework.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Aleksey Romodin
 */
@Getter
@AllArgsConstructor
@ToString
public class FullNameDto {
    private final String firstName;
    private final String middleName;
    private final String lastName;
}
