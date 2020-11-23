package com.example.sshomework.service.author;

import com.example.sshomework.dto.AuthorDto;
import com.example.sshomework.entity.Author;
import com.example.sshomework.mappers.AuthorMapper;
import com.example.sshomework.repository.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Aleksey Romodin
 */
@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

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
        author.getBooks().forEach(book -> book.setAuthorBook(author));
        authorRepository.save(author);

        return authorMapper.toDto(authorRepository.findFirstByOrderByIdDesc());
    }

    @Override
    public Boolean deleteAuthorById(Long id){
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            if (author.get().getBooks().size() == 0) {
                authorRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }

}
