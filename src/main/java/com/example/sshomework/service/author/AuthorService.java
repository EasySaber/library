package com.example.sshomework.service.author;

import com.example.sshomework.dto.AuthorDto;

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
}
