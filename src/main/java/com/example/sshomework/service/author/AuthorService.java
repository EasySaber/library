package com.example.sshomework.service.author;

import com.example.sshomework.dto.author.AuthorDto;
import com.example.sshomework.dto.author.AuthorSearchRequest;
import com.example.sshomework.entity.Author;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Aleksey Romodin
 */
public interface AuthorService {
    Optional<AuthorDto> getAuthorById(Long id);
    List<AuthorDto> getAll();
    AuthorDto addNewAuthor(AuthorDto authorDto);
    Boolean deleteAuthorById(Long id);
    List<AuthorDto> getAuthorInParameters(AuthorSearchRequest request);
}
