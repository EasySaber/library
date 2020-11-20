package com.example.sshomework.mappers;

import com.example.sshomework.dto.genre.GenreDto;
import com.example.sshomework.entity.Genre;
import org.mapstruct.Mapper;

/**
 * @author Aleksey Romodin
 */
@Mapper
public interface GenreMapper extends MapMapper<GenreDto, Genre>{

}
