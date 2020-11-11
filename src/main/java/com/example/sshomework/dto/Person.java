package com.example.sshomework.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate dateOfBirth;

    @JsonIgnore
    public String getFullName() {
        return firsName + " " + middleName + " " + lastName;
    }
}

