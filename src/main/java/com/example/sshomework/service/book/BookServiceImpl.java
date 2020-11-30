package com.example.sshomework.service.book;

import com.example.sshomework.dto.book.BookDto;
import com.example.sshomework.dto.book.BookSearchRequest;
import com.example.sshomework.dto.book.BookStatusDto;
import com.example.sshomework.entity.Author;
import com.example.sshomework.entity.Book;
import com.example.sshomework.entity.Genre;
import com.example.sshomework.mappers.Book.BookMapper;
import com.example.sshomework.mappers.Book.BookStatusMapper;
import com.example.sshomework.repository.author.AuthorRepository;
import com.example.sshomework.repository.book.BookRepository;
import com.example.sshomework.repository.GenreRepository;
import com.example.sshomework.specification.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final BookStatusMapper bookStatusMapper;

    @Override
    public BookDto addNewBook(BookDto bookDto) {

        Book book = bookMapper.toEntity(bookDto);
        Author author = authorRepository.findById(book.getAuthorBook().getId()).orElse(null);
        if (author != null) {
            book.setAuthorBook(author);
            if (book.getGenres().isEmpty()) {
                return null;
            } else {
                Set<Genre> addGenres = new HashSet<>();
                book.getGenres().forEach(genre ->
                        genreRepository.findById(genre.getId()).ifPresent(addGenres::add));

                if (!addGenres.isEmpty()) {
                    addGenres.forEach(genre -> genre.getBooks().add(book));
                    book.getGenres().clear();
                    book.setGenres(addGenres);

                } else {
                    return null;
                }
            }
            bookRepository.save(book);
            return bookMapper.toDto(bookRepository.findFirstByOrderByIdDesc());
        }
        return null;
    }

    @Override
    public Boolean deleteBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            if (book.get().getPersons().isEmpty()) {
                bookRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<BookDto> getByGenre(Long id) {
        return bookMapper.toDtoList(bookRepository.findAll().stream()
                .filter(book -> book.getGenres().stream().anyMatch(genre -> genre.getId().equals(id)))
                .collect(Collectors.toList()));
    }

    @Override
    public List<BookDto> getByAuthorFilter(String firstName, String middleName, String lastName) {

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
        return bookMapper.toDtoList(bookRepository.findAll(specification));
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
    public BookDto updateGenres(Long id, List<Long> genres){

        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent() & !genres.isEmpty()) {
            Set<Genre> genreSet = new HashSet<>();
            for (Long genreId : genres) {
                Optional<Genre> genre = genreRepository.findById(genreId);
                genre.ifPresent(genreSet::add);
            }
            if (genreSet.size() > 0) {
                book.get().getGenres().clear();
                book.get().setGenres(genreSet);
                bookRepository.save(book.get());
                return bookMapper.toDto(book.get());
            }
        }
        return null;
    }

    @Override
    public List<BookDto> getBookInParameters(BookSearchRequest request) {
        return bookMapper.toDtoList(bookRepository.customFilter(request));
    }

    @Override
    public List<BookStatusDto> getBooks() {
        return bookStatusMapper.toDtoList(bookRepository.findAll());
    }
}
