package com.example.sshomework.service.person;

import com.example.sshomework.dto.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aleksey Romodin
 */
@Service
public class PersonServiceImpl implements PersonService {

    private static final List<Person> personList = new ArrayList<>();
    static {
        personList.add(new Person("Иван","Иванович","Иванов", 25));
        personList.add(new Person("Петр","Петрович","Петров", 30));
        personList.add(new Person("Константин","Николаевич","Ципкин", 55));
    }

    @Override
    public List<Person> getAll() {
        return personList;
    }

    @Override
    public List<Person> findByFIO(String FIO) {
        return personList.stream()
                .filter(person -> (getFIO(person).equals(FIO)))
                .collect(Collectors.toList());
    }

    @Override
    public void deletePerson(String FIO) {
            personList.removeIf(person -> getFIO(person).equals(FIO));
    }

    @Override
    public List<Person> findByFirstName(String firstName) {
        return personList.stream()
                .filter(person -> person.getFirsName().equals(firstName))
                .collect(Collectors.toList());
    }

    @Override
    public void addNewPerson(Person person) {
        personList.add(person);
    }

    private String getFIO(Person person) {
        return person.getFirsName() + " " + person.getMiddleName() + " " + person.getLastName();
    }
}
