package com.example.sshomework.service.person;

import com.example.sshomework.dto.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        List<Person> findPersons = new ArrayList<>();
        for (Person person: personList) {
            if (getFIO(person.firsName, person.middleName, person.lastName).equals(FIO)) {
                findPersons.add(person);
            }
        }
        return findPersons;
    }

    @Override
    public void deletePerson(String FIO) {
        if (!personList.isEmpty()) {
            personList.removeIf(searchPerson ->
                    getFIO(searchPerson.firsName, searchPerson.middleName, searchPerson.lastName).equals(FIO));
        }
    }

    @Override
    public List<Person> findByFirstName(String firstName) {
        List<Person> findPersons = new ArrayList<>();
        for (Person person : personList) {
            if (person.firsName.equals(firstName)) {
                findPersons.add(person);
            }
        }
        return findPersons;
    }

    @Override
    public void addNewPerson(Person person) {
        personList.add(person);
    }

    private String getFIO(String f, String m, String l) {
        return f + " " + m + " " + l;
    }
}
