package com.example.sshomework.service.person;

import com.example.sshomework.dto.Person;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
public interface PersonService {
    void deletePerson(String FIO);
    void addNewPerson(Person person);
    List<Person> getAll();
    List<Person> findByFIO(String FIO);
    List<Person> findByFirstName(String firstName);
}
