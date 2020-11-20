package com.example.sshomework.dto;

import com.example.sshomework.dto.view.View;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * @author Aleksey Romodin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryCard {
    @Valid
    @JsonView(View.Public.class)
    private PersonDto personDto;
    @Valid
    @JsonView(View.Public.class)
    private BookDto bookDto;
    @NotNull
    @JsonView(View.Public.class)
    @Schema(pattern = "yyyy-MM-dd HH:mm:ss.SSSXXX", example = "2020-10-20 12:49:23.123+03:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSXXX")
    private ZonedDateTime dateTime;
}
