package com.example.sshomework.mappers;

import com.example.sshomework.dto.PersonDto;
import com.example.sshomework.entity.Person;
import org.mapstruct.Mapper;

/**
 * @author Aleksey Romodin
 */
@Mapper(componentModel = "spring", uses = {AuthorMapper.class, BookMapper.class})
public interface PersonMapper extends MapMapper<PersonDto, Person>{
}
