package com.example.sshomework.mappers;

import com.example.sshomework.dto.author.*;
import com.example.sshomework.entity.*;
import org.mapstruct.*;

/**
 * @author Aleksey Romodin
 */
@Mapper
public interface AuthorMapper extends MapMapper<AuthorDto, Author> {
}
