package com.example.sshomework.service.libraryCard;

import com.example.sshomework.dto.LibraryCardDto;
import com.example.sshomework.entity.Book;
import com.example.sshomework.entity.LibraryCard;
import com.example.sshomework.entity.Person;
import com.example.sshomework.exception.PersonBookDebtException;
import com.example.sshomework.mappers.LibraryCardMapper;
import com.example.sshomework.repository.LibraryCardRepository;
import com.example.sshomework.repository.PersonRepository;
import com.example.sshomework.repository.book.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Aleksey Romodin
 */
@Service
public class LibraryCardServiceImpl implements LibraryCardService {

    private final LibraryCardRepository libraryCardRepository;
    private final BookRepository bookRepository;
    private final PersonRepository personRepository;
    private final LibraryCardMapper libraryCardMapper;

    @Autowired
    public LibraryCardServiceImpl(LibraryCardRepository libraryCardRepository,
                                  BookRepository bookRepository,
                                  PersonRepository personRepository,
                                  LibraryCardMapper libraryCardMapper)
    {
        this.libraryCardRepository = libraryCardRepository;
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
        this.libraryCardMapper = libraryCardMapper;
    }

    @Override
    public List<LibraryCardDto> getDebtors() {
        List<LibraryCard> libraryCards = libraryCardRepository.findAll();

        return libraryCardMapper.toDtoList(
                libraryCards.stream().filter(libraryCard -> (libraryCard.daysDept() > 0))
                        .collect(Collectors.toList()));
    }

    @Override
    public Optional<LibraryCardDto> prolongation(Long personId, Long bookId, Long days) {
        LibraryCard libraryCard = libraryCardRepository.findByBookIdAndPersonId(bookId, personId)
                .orElseThrow(() -> new NotFoundException("Карточка не найдена."));

        ZonedDateTime dateReturn = libraryCard.getDateTimeReturn().plusDays(days);
        libraryCard.setDateTimeReturn(dateReturn);
        libraryCardRepository.save(libraryCard);
        return Optional.of(libraryCard).map(libraryCardMapper::toDto);
    }

    @Override
    public Optional<LibraryCardDto> addNewCard(Long personId, Long bookId) throws PersonBookDebtException {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Книга не найдена."));

        List<LibraryCard> libraryCards = libraryCardRepository.findByPersonId(personId);
        if (libraryCards.stream().noneMatch(libraryCard -> libraryCard.daysDept() > 0)) {
            LibraryCard addCard = new LibraryCard();
            addCard.setBook(book);
            addCard.setPerson(person);
            libraryCardRepository.save(addCard);
            return Optional.of(addCard).map(libraryCardMapper::toDto);
        }
        throw new PersonBookDebtException("У пользователя имеются задолженности.");
    }

}
