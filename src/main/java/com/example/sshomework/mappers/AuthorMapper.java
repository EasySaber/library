package com.example.sshomework.mappers;

import com.example.sshomework.dto.author.AuthorDto;
import com.example.sshomework.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Aleksey Romodin
 */
@Mapper
public interface AuthorMapper extends MapMapper<AuthorDto, Author> {
}
