package com.example.sshomework.service.person;

import com.example.sshomework.dto.PersonDto;
import com.example.sshomework.entity.Person;
import com.example.sshomework.mappers.PersonMapper;
import com.example.sshomework.repository.BookRepository;
import com.example.sshomework.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Boolean deletePersonByFullName(String firstName, String middleName, String lastName) {
        if (!personRepository.findByFirstNameAndMiddleNameAndLastName(firstName, middleName, lastName).isEmpty()) {
            personRepository.deleteByFirstNameAndMiddleNameAndLastName(firstName, middleName, lastName);
            return true;
        }
        return false;
    }

    @Override
    public PersonDto getBooksByAuthorId(Long id) {
        if (personRepository.findById(id).isPresent()) {
            return personMapper.toDto(personRepository.findById(id).get());
        }
        return null;
    }

    @Override
    public PersonDto addNewPerson(PersonDto personDto) {
        personRepository.save(personMapper.toEntity(personDto));
        return personMapper.toDto(personRepository.findFirstByOrderByIdDesc());
    }

    @Override
    public Optional<PersonDto> updatePerson(PersonDto personDto) {
        if (personRepository.findById(personDto.getId()).isPresent()) {
            Person person = personMapper.toEntity(personDto);
            personRepository.save(person);
            return Optional.of(personMapper.toDto(person));
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
        if (personRepository.findById(personId).isPresent() & bookRepository.findById(bookId).isPresent()) {
            Person person = personRepository.findById(personId).get();
            if (operation) {
                if (person.getBooks().stream().allMatch(book -> book.getId().equals(bookId))) {
                    person.getBooks().removeIf(book->(book.getId().equals(bookId)));
                } else {
                    return null;
                }
            } else {
                person.getBooks().add(bookRepository.findById(bookId).get());
            }
            personRepository.save(person);
            return personMapper.toDto(personRepository.findById(personId).get());
        }
        return null;
    }
}
