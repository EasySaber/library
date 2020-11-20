package com.example.sshomework.service.genre;

import com.example.sshomework.dto.genre.GenreDto;
import com.example.sshomework.dto.genre.GenreStatisticsProjection;
import com.example.sshomework.mappers.GenreMapper;
import com.example.sshomework.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService{

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public List<GenreDto> getAll(){
        return genreMapper.toDtoList(genreRepository.findAll());
    }

    @Override
    public void addNewGenre(GenreDto genreDto){
        genreRepository.save(genreMapper.toEntity(genreDto));
    }

    @Override
    public List<GenreStatisticsProjection> getGenreStatistics(){
        return genreRepository.getGenreStatistics();
    }
}
