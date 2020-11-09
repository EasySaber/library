package com.example.sshomework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @author Aleksey Romodin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private String firsName;
    private String middleName;
    private String lastName;
    private Integer age;
}

