package com.example.sshomework.service.author;

import com.example.sshomework.dto.AuthorDto;
import com.example.sshomework.entity.Book;
import com.example.sshomework.mappers.AuthorMapper;
import com.example.sshomework.repository.AuthorRepository;
import com.example.sshomework.repository.BookRepository;
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
    private final BookRepository bookRepository;


    @Override
    public Optional<AuthorDto> getAuthorById(Long id){
        return authorRepository.findById(id).isPresent() ?
               Optional.of(authorMapper.toDto(authorRepository.findById(id).get())) : Optional.empty();
    }

    @Override
    public List<AuthorDto> getAll(){
        return authorMapper.toDtoList(authorRepository.findAll());
    }

    @Override
    public AuthorDto addNewAuthor(AuthorDto authorDto){

        authorRepository.save(authorMapper.toEntity(authorDto));

        //Сохраняем книги, если они добавлены
        if (authorDto.getBooks() != null) {
            for (Book book : authorMapper.toEntity(authorDto).getBooks()) {
                book.setAuthorBook(authorRepository.findFirstByOrderByIdDesc());
                bookRepository.save(book);
            }
        }
        return authorMapper.toDto(authorRepository.findFirstByOrderByIdDesc());
    }

    @Override
    public Boolean deleteAuthorById(Long id){
        if (authorRepository.findById(id).isPresent()) {
            if (bookRepository.findBookByAuthorBookId(id).isEmpty()) {
                authorRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }

}
