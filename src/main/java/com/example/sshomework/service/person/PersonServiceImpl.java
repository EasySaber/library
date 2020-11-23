package com.example.sshomework.service.person;

import com.example.sshomework.dto.PersonDto;
import com.example.sshomework.entity.Book;
import com.example.sshomework.entity.Person;
import com.example.sshomework.mappers.PersonMapper;
import com.example.sshomework.repository.BookRepository;
import com.example.sshomework.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Aleksey Romodin
 */
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final BookRepository bookRepository;
    private final PersonMapper personMapper;

    @Override
    public List<PersonDto> getAll() {
        return personMapper.toDtoList(personRepository.findAll());
    }

    @Override
    public Boolean deletePersonsByFullName(String firstName, String middleName, String lastName) {
        List<Person> persons =
                personRepository.findByFirstNameAndMiddleNameAndLastName(firstName, middleName, lastName);
        if (!persons.isEmpty()) {
            personRepository.deleteAll(persons);
            return true;
        }
        return false;
    }

    @Override
    public PersonDto getBooksByAuthorId(Long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.map(personMapper::toDto).orElse(null);
    }

    @Override
    public PersonDto addNewPerson(PersonDto personDto) {
        personRepository.save(personMapper.toEntity(personDto));
        return personMapper.toDto(personRepository.findFirstByOrderByIdDesc());
    }

    @Override
    public Optional<PersonDto> updatePerson(PersonDto personDto) {

        Person personIncoming= personMapper.toEntity(personDto);
        Person personStored = personRepository.findById(personIncoming.getId()).orElse(null);

        if (personStored != null) {
            personStored.setFirstName(personIncoming.getFirstName());
            personStored.setMiddleName(personIncoming.getMiddleName());
            personStored.setLastName(personIncoming.getLastName());
            personStored.setDateOfBirth(personIncoming.getDateOfBirth());
            personRepository.save(personStored);
            return Optional.of(personMapper.toDto(personStored));
        }
        return Optional.empty();
    }

    @Override
    public Boolean deletePersonById(Long id) {
        if (personRepository.findById(id).isPresent()) {
            personRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public PersonDto addNewPostLibraryCard(Long personId, Long bookId) {
        return updateLibraryCard(personId, bookId, false);
    }

    @Override
    public PersonDto deletePostLibraryCard(Long personId, Long bookId) {
        return  updateLibraryCard(personId, bookId, true);
    }

    private PersonDto updateLibraryCard(Long personId, Long bookId, Boolean operation) {
        Person person = personRepository.findById(personId).orElse(null);
        Book bookIncoming = bookRepository.findById(bookId).orElse(null);
        if (person != null & bookIncoming != null) {
            Set<Book> bookSet = person.getBooks();
            if (operation) { //Удаление книги

                bookSet.removeIf(book -> (book.getId().equals(bookId)));
            } else {         //Добавление книги
                bookSet.add(bookIncoming);
                person.setBooks(bookSet);
            }
            personRepository.save(person);
            return personMapper.toDto(person);
        }
        return null;
    }
}
