package com.example.sshomework.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Aleksey Romodin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @JsonView(View.Public.class)
    @NotBlank(message = "Пустое значение")
    @Size(max = 200)
    private String name;
    @JsonView(View.Public.class)
    @NotBlank(message = "Пустое значение")
    @Size(max = 200)
    private String author;
    @JsonView(View.All.class)
    @NotBlank(message = "Пустое значение")
    @Size(max = 200)
    private String genre;
}
