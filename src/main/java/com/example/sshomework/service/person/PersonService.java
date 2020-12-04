package com.example.sshomework.service.person;

import com.example.sshomework.dto.FullNameDto;
import com.example.sshomework.dto.PersonDto;

import java.util.List;
import java.util.Optional;

/**
 * @author Aleksey Romodin
 */
public interface PersonService {
    void deletePersonsByFullName(FullNameDto fullNameDto);

    Optional<PersonDto> addNewPerson(PersonDto personDto);

    Optional<PersonDto> updatePerson(PersonDto personDto);

    void deletePersonById(Long id);

    List<PersonDto> getAll();

    Optional<PersonDto> getBooksByAuthorId(Long id);

    Optional<PersonDto> addNewPostLibraryCard(Long personId, Long bookId);

    Optional<PersonDto> deletePostLibraryCard(Long personId, Long bookId);

    Optional<PersonDto> getListUserBooks();
}
