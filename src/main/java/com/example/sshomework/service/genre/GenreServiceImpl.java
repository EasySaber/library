package com.example.sshomework.service.genre;

import com.example.sshomework.dto.genre.GenreDto;
import com.example.sshomework.dto.genre.GenreStatisticsProjection;
import com.example.sshomework.entity.Genre;
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

    @Override
    public Integer deleteGenre(Long id) {
        Genre genreStored = genreRepository.findById(id).orElse(null);
        if (genreStored != null) {
            if (genreStored.getBooks().stream().noneMatch(book -> book.getGenres().size() == 1)) {
                genreRepository.delete(genreStored);
                return 200;
            } else {
                return 400;
            }
        }
        return  404;
    }

}
