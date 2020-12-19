package com.example.sshomework.repository.author;

import com.example.sshomework.dto.author.AuthorSearchRequest;
import com.example.sshomework.entity.Author;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
public interface CustomAuthorRepository {
    List<Author> customFilter(AuthorSearchRequest request);
}
