package com.example.sshomework.mappers;

import com.example.sshomework.dto.AuthorDto;
import com.example.sshomework.entity.Author;
import org.mapstruct.Mapper;

/**
 * @author Aleksey Romodin
 */
@Mapper
public interface AuthorMapper extends MapMapper<AuthorDto, Author> {

}
