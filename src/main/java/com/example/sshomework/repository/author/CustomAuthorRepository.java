package com.example.sshomework.repository.author;

import com.example.sshomework.dto.AuthorDto;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author Aleksey Romodin
 */
@NoRepositoryBean
public interface CustomAuthorRepository {
    AuthorDto customFilter();
}
