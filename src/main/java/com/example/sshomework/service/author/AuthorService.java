package com.example.sshomework.service.author;

import com.example.sshomework.dto.author.AuthorDto;
import com.example.sshomework.dto.author.AuthorSearchRequest;
import com.example.sshomework.exception.DeleteRelatedDataException;

import java.util.List;
import java.util.Optional;

/**
 * @author Aleksey Romodin
 */
public interface AuthorService {
    Optional<AuthorDto> getAuthorById(Long id);
    List<AuthorDto> getAll();
    Optional<AuthorDto> addNewAuthor(AuthorDto authorDto);
    void deleteAuthorById(Long id) throws DeleteRelatedDataException;
    List<AuthorDto> getAuthorInParameters(AuthorSearchRequest request);
}
