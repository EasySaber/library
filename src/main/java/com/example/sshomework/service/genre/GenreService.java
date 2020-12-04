package com.example.sshomework.service.genre;

import com.example.sshomework.dto.genre.GenreDto;
import com.example.sshomework.dto.genre.GenreStatisticsProjection;
import com.example.sshomework.exception.NotUniqueValueException;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
public interface GenreService {
    List<GenreDto> getAll();
    void addNewGenre(GenreDto genreDto) throws NotUniqueValueException;
    List<GenreStatisticsProjection> getGenreStatistics();
    void deleteGenre(String genreName);
}
