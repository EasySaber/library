package com.example.sshomework.mappers;

import com.example.sshomework.dto.PersonDto;
import com.example.sshomework.entity.Person;
import com.example.sshomework.mappers.Book.BookMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Aleksey Romodin
 */
@Mapper(uses = LibraryCardMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PersonMapper extends MapMapper<PersonDto, Person>{

    @Mapping(target = "booksDto.personDto", ignore = true)
    @Mapping(target = "booksDto", source = "books")
    PersonDto toDto(Person person);

}
