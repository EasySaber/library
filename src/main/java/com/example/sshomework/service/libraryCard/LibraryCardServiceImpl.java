package com.example.sshomework.service.libraryCard;

import com.example.sshomework.dto.LibraryCardDto;
import com.example.sshomework.entity.Book;
import com.example.sshomework.entity.LibraryCard;
import com.example.sshomework.entity.Person;
import com.example.sshomework.mappers.LibraryCardMapper;
import com.example.sshomework.repository.book.BookRepository;
import com.example.sshomework.repository.LibraryCardRepository;
import com.example.sshomework.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aleksey Romodin
 */
@Service
@RequiredArgsConstructor
public class LibraryCardServiceImpl implements LibraryCardService {

    private final LibraryCardMapper libraryCardMapper;
    private final LibraryCardRepository libraryCardRepository;
    private final BookRepository bookRepository;
    private final PersonRepository personRepository;

    @Override
    public List<LibraryCardDto> getDebtors() {
        List<LibraryCard> libraryCards = libraryCardRepository.findAll();

        return libraryCardMapper.toDtoList(
                libraryCards.stream().filter(libraryCard -> (libraryCard.daysDept() > 0))
                        .collect(Collectors.toList()));
    }

    @Override
    public LibraryCardDto prolongation(Long personId, Long bookId, Long days) {
        LibraryCard libraryCard = libraryCardRepository.findByBookIdAndPersonId(bookId, personId);
        if ((libraryCard != null) & (days > 0)) {
            ZonedDateTime dateReturn = libraryCard.getDateTimeReturn().plusDays(days);
            libraryCard.setDateTimeReturn(dateReturn);
            libraryCardRepository.save(libraryCard);
            return libraryCardMapper.toDto(libraryCard);
        }
        return null;
    }

    @Override
    public LibraryCardDto addNewCard(Long personId, Long bookId) {
        Person person = personRepository.findById(personId).orElse(null);
        Book book = bookRepository.findById(bookId).orElse(null);

        if ((person != null) & (book != null)) {
            List<LibraryCard> libraryCards = libraryCardRepository.findByPersonId(personId);
            if (libraryCards.stream().noneMatch(libraryCard -> libraryCard.daysDept() > 0)) {
                LibraryCard addCard = new LibraryCard();
                addCard.setBook(book);
                addCard.setPerson(person);
                libraryCardRepository.save(addCard);
                return libraryCardMapper.toDto(addCard);
            }
        }
        return null;
    }

}
