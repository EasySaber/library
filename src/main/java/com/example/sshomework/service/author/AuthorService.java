package com.example.sshomework.service.author;

import com.example.sshomework.dto.author.*;

import java.util.*;

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
