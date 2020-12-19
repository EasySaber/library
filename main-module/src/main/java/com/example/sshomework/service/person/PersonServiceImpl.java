package com.example.sshomework.service.person;

import com.example.sshomework.aspect.annotation.LoggerCrud;
import com.example.sshomework.dto.FullNameDto;
import com.example.sshomework.dto.PersonDto;
import com.example.sshomework.entity.Book;
import com.example.sshomework.entity.LibraryCard;
import com.example.sshomework.entity.Person;
import com.example.sshomework.mappers.PersonMapper;
import com.example.sshomework.repository.PersonRepository;
import com.example.sshomework.repository.book.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final BookRepository bookRepository;
    private final PersonMapper personMapper;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository,
                             BookRepository bookRepository,
                             PersonMapper personMapper)
    {
        this.personRepository = personRepository;
        this.bookRepository = bookRepository;
        this.personMapper = personMapper;
    }

    @Override
    public List<PersonDto> getAll() {
        return personMapper.toDtoList(personRepository.findAll());
    }

    @LoggerCrud(operation = LoggerCrud.Operation.DELETE)
    @Override
    public void deletePersonsByFullName(FullNameDto fullName) {
        List<Person> persons = personRepository.findByFirstNameAndMiddleNameAndLastName(
                fullName.getFirstName(), fullName.getMiddleName(), fullName.getLastName());
        if (!persons.isEmpty()) {
            personRepository.deleteAll(persons);
        } else {
            throw new NotFoundException("Совпадения не найдены.");
        }
    }

    @Override
    public Optional<PersonDto> getBooksByAuthorId(Long id) {
        return Optional.of(getPerson(id)).map(personMapper::toDto);
    }

    @LoggerCrud(operation = LoggerCrud.Operation.CREATE)
    @Override
    public Optional<PersonDto> addNewPerson(PersonDto personDto) {
        personRepository.save(personMapper.toEntity(personDto));
        return personRepository.findFirstByOrderByIdDesc().map(personMapper::toDto);
    }

    @LoggerCrud(operation = LoggerCrud.Operation.UPDATE)
    @Override
    public Optional<PersonDto> updatePerson(PersonDto personDto) {
        Person personIncoming = personMapper.toEntity(personDto);
        Person personStored = getPerson(personIncoming.getId());

        personStored.setFirstName(personIncoming.getFirstName());
        personStored.setMiddleName(personIncoming.getMiddleName());
        personStored.setLastName(personIncoming.getLastName());
        personStored.setDateOfBirth(personIncoming.getDateOfBirth());
        personRepository.save(personStored);
        return Optional.of(personMapper.toDto(personStored));

    }

    @LoggerCrud(operation = LoggerCrud.Operation.DELETE)
    @Override
    public void deletePersonById(Long id) {
        personRepository.delete(getPerson(id));
    }

    @LoggerCrud(operation = LoggerCrud.Operation.CREATE)
    @Override
    public Optional<PersonDto> addNewPostLibraryCard(Long personId, Long bookId) {
        return updateLibraryCard(personId, bookId, false);
    }

    @LoggerCrud(operation = LoggerCrud.Operation.DELETE)
    @Override
    public Optional<PersonDto> deletePostLibraryCard(Long personId, Long bookId) {
        return updateLibraryCard(personId, bookId, true);
    }

    @Override
    public Optional<PersonDto> getListUserBooks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        return personRepository.findByAccount_Username(user.getUsername()).map(personMapper::toDto);
    }

    private Optional<PersonDto> updateLibraryCard(Long personId, Long bookId, Boolean operation) {
        Person person = getPerson(personId);
        Book bookIncoming = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Книга не найдена."));

            Set<LibraryCard> libraryCards;
            if (operation) { //Удаление книги
                libraryCards = Optional.ofNullable(person.getBooks()).orElseThrow(() -> new NotFoundException("У пользователя нет взятых книг."));
                libraryCards.removeIf(libraryCard -> (libraryCard.getBook().getId().equals(bookId)));
            } else {         //Добавление книги
                libraryCards = Optional.of(person.getBooks()).orElse(new HashSet<>());
                LibraryCard newCard = new LibraryCard();
                newCard.setBook(bookIncoming);
                newCard.setPerson(person);
                libraryCards.add(newCard);
            }
            person.getBooks().clear();
            person.setBooks(libraryCards);
            personRepository.save(person);
            return Optional.of(person).map(personMapper::toDto);
    }

    private Person getPerson(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
    }

}
