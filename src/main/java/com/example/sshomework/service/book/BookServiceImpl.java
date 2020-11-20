package com.example.sshomework.service.book;

import com.example.sshomework.dto.BookDto;
import com.example.sshomework.dto.genre.GenreDto;
import com.example.sshomework.entity.Book;
import com.example.sshomework.entity.Genre;
import com.example.sshomework.mappers.BookMapper;
import com.example.sshomework.repository.AuthorRepository;
import com.example.sshomework.repository.BookRepository;
import com.example.sshomework.repository.GenreRepository;
import com.example.sshomework.specification.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Aleksey Romodin
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;

    @Override
    public Book addNewBook(BookDto bookDto) {

        Set<GenreDto> genreDtoSet = bookDto.getGenres();

        for (GenreDto genre : genreDtoSet) {
            if (!genreRepository.findById(genre.getId()).isPresent()) {
                return null;
            }
        }

        if (authorRepository.findById(bookDto.getAuthorBookDto().getId()).isPresent()) {
            bookRepository.save(bookMapper.toEntity(bookDto));
            return bookRepository.findFirstByOrderByIdDesc();
        }
        return null;
    }

    @Override
    public Boolean deleteBookById(Long id) {
        if (bookRepository.findById(id).isPresent()) {
            if (bookRepository.findById(id).get().getPersons().isEmpty()) {
                bookRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Book> getByGenre(Long id) {
        List<Book> books = new ArrayList<>();
        if (genreRepository.findById(id).isPresent()) {
            for (Book book : bookRepository.findAll()) {
                if (book.getGenres().stream().anyMatch(genre -> genre.getId().equals(id))) {
                    books.add(book);
                }
            }
        }
        return books;
    }

    @Override
    public List<Book> getByAuthorFilter(String firstName, String middleName, String lastName) {

        Specification<Book> specification = null;

        // Формируем условия для запроса
        if (firstName != null) {
            specification = draftSpecification(null, "firstName", firstName);
        }
        if (middleName != null) {
            specification = draftSpecification(specification, "middleName", middleName);
        }
        if (lastName != null) {
            specification = draftSpecification(specification, "lastName", lastName);
        }
        return bookRepository.findAll(specification);
    }

    private Specification<Book> draftSpecification(
            Specification<Book> specification, String columnName, String optionalName)
    {
        if (optionalName != null) {
            if (specification == null) {
                specification = Specification.where(new BookSpecification(columnName, optionalName));
            } else {
                specification = specification.and(new BookSpecification(columnName, optionalName));
            }
        }
        return specification;
    }

    @Override
    public Book updateGenres(Long id, List<Long> genres){

        if (bookRepository.findById(id).isPresent() & !genres.isEmpty()) {
           Set<Genre> genreSet = new HashSet<>();
           for (Long genreId : genres) {
               if (genreRepository.findById(genreId).isPresent()) {
                   genreSet.add(genreRepository.findById(genreId).get());
               }
           }
           if (genreSet.size() > 0) {
               Book book = bookRepository.findById(id).get();
               book.getGenres().clear();
               book.setGenres(genreSet);
               bookRepository.save(book);
               return bookRepository.findById(id).get();
           }
        }
        return null;
    }
}
