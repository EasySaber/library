package com.example.sshomework.service.person;

import com.example.sshomework.dto.PersonDto;

import java.util.List;
import java.util.Optional;

/**
 * @author Aleksey Romodin
 */
public interface PersonService {
    Boolean deletePersonByFullName(String firstName, String middleName, String lastName);
    PersonDto addNewPerson(PersonDto personDto);
    Optional<PersonDto> updatePerson(PersonDto personDto);
    Boolean deletePersonById(Long id);
    List<PersonDto> getAll();
    PersonDto getBooksByAuthorId(Long id);
    PersonDto addNewPostLibraryCard(Long personId, Long bookId);
    PersonDto deletePostLibraryCard(Long personId, Long bookId);
}
