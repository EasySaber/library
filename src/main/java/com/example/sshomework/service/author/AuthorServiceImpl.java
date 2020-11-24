package com.example.sshomework.service.author;

import com.example.sshomework.dto.AuthorDto;
import com.example.sshomework.entity.Author;
import com.example.sshomework.entity.Genre;
import com.example.sshomework.mappers.AuthorMapper;
import com.example.sshomework.repository.AuthorRepository;
import com.example.sshomework.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Aleksey Romodin
 */
@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final GenreRepository genreRepository;

    @Override
    public Optional<AuthorDto> getAuthorById(Long id){
        Optional<Author> author = authorRepository.findById(id);
        return author.map(authorMapper::toDto);
    }

    @Override
    public List<AuthorDto> getAll(){
        return authorMapper.toDtoList(authorRepository.findAll());
    }

    @Override
    public AuthorDto addNewAuthor(AuthorDto authorDto){

        Author author = authorMapper.toEntity(authorDto);

        author.getBooks().forEach(book -> {
            book.setAuthorBook(author);
            Set<Genre> genresBook = new HashSet<>();
            book.getGenres().forEach(genre ->
                genresBook.add(genreRepository.findById(genre.getId()).orElse(null)));
            book.setGenres(genresBook);
            });

        authorRepository.save(author);

        return authorMapper.toDto(authorRepository.findFirstByOrderByIdDesc());
    }

    @Override
    public Boolean deleteAuthorById(Long id){
        Author author = authorRepository.findById(id).orElse(null);
        if (author != null) {
            if (author.getBooks().stream().noneMatch(book -> book.getPersons().size() > 0)) {
                authorRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }

}
