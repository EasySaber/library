package com.example.sshomework.service.book;

import com.example.sshomework.dto.FullNameDto;
import com.example.sshomework.dto.book.BookDto;
import com.example.sshomework.dto.book.BookSearchRequest;
import com.example.sshomework.dto.book.BookStatusDto;
import com.example.sshomework.entity.Author;
import com.example.sshomework.entity.Book;
import com.example.sshomework.entity.Genre;
import com.example.sshomework.exception.DeleteRelatedDataException;
import com.example.sshomework.mappers.Book.BookMapper;
import com.example.sshomework.mappers.Book.BookStatusMapper;
import com.example.sshomework.repository.GenreRepository;
import com.example.sshomework.repository.author.AuthorRepository;
import com.example.sshomework.repository.book.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Aleksey Romodin
 */
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    private final BookStatusMapper bookStatusMapper;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository,
                           GenreRepository genreRepository,
                           AuthorRepository authorRepository,
                           BookMapper bookMapper,
                           BookStatusMapper bookStatusMapper)
    {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
        this.bookStatusMapper = bookStatusMapper;
    }

    @Override
    public Optional<BookDto> addNewBook(BookDto bookDto) {

        Book book = bookMapper.toEntity(bookDto);
        Author author = authorRepository.findById(book.getAuthorBook().getId())
                .orElseThrow(() -> new NotFoundException("Автор не найден"));

        Set<Genre> genres = Optional.ofNullable(book.getGenres())
                .orElseThrow(() -> new NotFoundException("Необходимо выбрать жанры."));

        book.setAuthorBook(author);

        Set<Genre> addGenres = new HashSet<>();
        genres.forEach(genre -> genreRepository.findByGenreName(genre.getGenreName()).ifPresent(addGenres::add));
        if (!addGenres.isEmpty()) {
            addGenres.forEach(genre -> genre.getBooks().add(book));
            book.getGenres().clear();
            book.setGenres(addGenres);
        } else {
            throw new NotFoundException("Жанры не найдены.");
        }

        bookRepository.save(book);
        return bookRepository.findFirstByOrderByIdDesc().map(bookMapper::toDto);
    }

    @Override
    public void deleteBookById(Long id) throws DeleteRelatedDataException {
        Book book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Книга не найдена."));
        Optional.ofNullable(book.getPersons()).ifPresent(person -> {
            if (!person.isEmpty()) {
                throw new DeleteRelatedDataException("Невозможно удалить книгу, находится в пользовании.");
            }
        });
        bookRepository.delete(book);
    }

    @Override
    public List<BookDto> getByGenre(String genreName) {
        Genre genre = genreRepository.findByGenreName(genreName)
                .orElseThrow(() -> new NotFoundException("Жанр не найден."));
        return bookMapper.toDtoList(
                bookRepository.findAll().stream()
                        .filter(book -> Optional.ofNullable(book.getGenres()).isPresent())
                .filter(book -> book.getGenres().stream().anyMatch(g -> g.equals(genre)))
                .collect(Collectors.toList()));
    }

    @Override
    public List<BookDto> getByAuthorFilter(FullNameDto request) {

        List<Predicate> predicates = new ArrayList<>();

        CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> cQuery = cBuilder.createQuery(Book.class);
        Root<Book> book = cQuery.from(Book.class);

        String firstName = request.getFirstName();
        String middleName = request.getMiddleName();
        String lastName = request.getLastName();

        // Формируем условия для запроса
        if (firstName != null) {
            predicates.add(cBuilder.equal(book.get("authorBook").get("firstName"), firstName));
        }
        if (middleName != null) {
            predicates.add(cBuilder.equal(book.get("authorBook").get("middleName"), middleName));
        }
        if (lastName != null) {
            predicates.add(cBuilder.equal(book.get("authorBook").get("lastName"), lastName));
        }

        if (!predicates.isEmpty()) {
            cQuery.where(predicates.toArray(new Predicate[0]));
        }

        return bookMapper.toDtoList(entityManager.createQuery(cQuery).getResultList());
    }

    @Override
    public Optional<BookDto> updateGenres(Long id, List<String> genres){
        if (!genres.isEmpty()) {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Книга не найдена."));

            Set<Genre> genreSet = new HashSet<>();
            genres.forEach(genre -> genreRepository.findByGenreName(genre).map(genreSet::add));
            if (genreSet.size() > 0) {
                book.getGenres().clear();
                book.setGenres(genreSet);
                bookRepository.save(book);
                return Optional.of(book).map(bookMapper::toDto);
            }
        }
        throw new NotFoundException("Жанры не найдены.");
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
