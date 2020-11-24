package com.example.sshomework.service.genre;

import com.example.sshomework.dto.genre.GenreDto;
import com.example.sshomework.dto.genre.GenreStatisticsProjection;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
public interface GenreService {
    List<GenreDto> getAll();
    void addNewGenre(GenreDto genreDto);
    List<GenreStatisticsProjection> getGenreStatistics();
    Integer deleteGenre(Long id);
}
