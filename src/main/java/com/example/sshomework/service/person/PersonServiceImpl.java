package com.example.sshomework.service.person;

import com.example.sshomework.dto.Person;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        personList.add(new Person("Иван","Иванович","Иванов",
                LocalDate.parse("1980-10-18", formatter)));
        personList.add(new Person("Петр","Петрович","Петров",
                LocalDate.parse("1982-11-17", formatter)));
        personList.add(new Person("Константин","Николаевич","Ципкин",
                LocalDate.parse("1983-12-01", formatter)));
    }

    @Override
    public List<Person> getAll() {
        return personList;
    }

    @Override
    public List<Person> findByFullName(String fullName) {
        return personList.stream()
                .filter(person -> (person.getFullName().equals(fullName)))
                .collect(Collectors.toList());
    }

    @Override
    public void deletePerson(String fullName) {
        personList.removeIf(person ->person.getFullName().equals(fullName));
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

}
