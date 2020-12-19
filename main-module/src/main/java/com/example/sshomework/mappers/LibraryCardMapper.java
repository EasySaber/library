package com.example.sshomework.mappers;

import com.example.sshomework.dto.LibraryCardDto;
import com.example.sshomework.entity.LibraryCard;
import com.example.sshomework.mappers.Book.BookMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Aleksey Romodin
 */

@Mapper(uses = BookMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LibraryCardMapper extends MapMapper<LibraryCardDto, LibraryCard>{
    @Mapping(target = "bookDto", source = "book")
    @Mapping(target = "personDto", source = "person")
    LibraryCardDto toDto(LibraryCard libraryCard);
}
