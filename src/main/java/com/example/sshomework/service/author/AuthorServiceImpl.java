package com.example.sshomework.service.author;

import com.example.sshomework.dto.author.AuthorDto;
import com.example.sshomework.dto.author.AuthorSearchRequest;
import com.example.sshomework.entity.Author;
import com.example.sshomework.entity.Genre;
import com.example.sshomework.exception.DeleteRelatedDataException;
import com.example.sshomework.mappers.AuthorMapper;
import com.example.sshomework.repository.GenreRepository;
import com.example.sshomework.repository.author.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Aleksey Romodin
 */
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final AuthorMapper authorMapper;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, GenreRepository genreRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public Optional<AuthorDto> getAuthorById(Long id){
        return authorRepository.findById(id).map(authorMapper::toDto);
    }

    @Override
    public List<AuthorDto> getAll(){
        return authorMapper.toDtoList(authorRepository.findAll());
    }

    @Override
    public Optional<AuthorDto> addNewAuthor(AuthorDto authorDto){

        Author author = authorMapper.toEntity(authorDto);

        author.getBooks().forEach(book -> {
            book.setAuthorBook(author);
            Set<Genre> genresBook = new HashSet<>();
            book.getGenres().forEach(genre ->
                    genreRepository.findByGenreName(genre.getGenreName()).ifPresent(genresBook::add));
            book.setGenres(genresBook);
        });
        authorRepository.save(author);

        return authorRepository.findFirstByOrderByIdDesc().map(authorMapper::toDto);
    }

    @Override
    public void deleteAuthorById(Long id) throws DeleteRelatedDataException {
        Author author = authorRepository.findById(id).orElseThrow(() -> new NotFoundException("Автор не найден"));
        if (author.getBooks().stream().anyMatch(book -> book.getPersons().size() > 0)) {
            throw new DeleteRelatedDataException("Удаление невозможно. Книги автора находяться в пользовании.");
        }
        authorRepository.deleteById(id);
    }

    @Override
    public List<AuthorDto> getAuthorInParameters(AuthorSearchRequest request){
        return authorMapper.toDtoList(authorRepository.customFilter(request));
    }
}
