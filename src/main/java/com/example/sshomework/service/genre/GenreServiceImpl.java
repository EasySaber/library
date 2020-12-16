package com.example.sshomework.service.genre;

import com.example.sshomework.aspect.annotation.LoggerCrud;
import com.example.sshomework.dto.genre.GenreDto;
import com.example.sshomework.dto.genre.GenreStatisticsProjection;
import com.example.sshomework.entity.Genre;
import com.example.sshomework.exception.NotUniqueValueException;
import com.example.sshomework.mappers.GenreMapper;
import com.example.sshomework.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Service
public class GenreServiceImpl implements GenreService{

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    @Override
    public List<GenreDto> getAll(){
        return genreMapper.toDtoList(genreRepository.findAll());
    }

    @LoggerCrud(operation = LoggerCrud.Operation.CREATE)
    @Override
    public void addNewGenre(GenreDto genreDto) throws NotUniqueValueException {
        try {
            genreRepository.save(genreMapper.toEntity(genreDto));
        } catch (DataIntegrityViolationException ex) {
            if (((SQLException) ex.getMostSpecificCause()).getSQLState().equals("23505")) {
                throw new NotUniqueValueException("Жанр с таким именем уже существует");
            }
        }
    }

    @Override
    public List<GenreStatisticsProjection> getGenreStatistics(){
        return genreRepository.getGenreStatistics();
    }

    @LoggerCrud(operation = LoggerCrud.Operation.DELETE)
    @Override
    public void deleteGenre(String genreName) {
        Genre genreStored = genreRepository.findByGenreName(genreName)
                .orElseThrow(() -> new NotFoundException("Жанр не найден."));
            genreRepository.delete(genreStored);
    }

}
