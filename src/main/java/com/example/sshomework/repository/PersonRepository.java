package com.example.sshomework.repository;

import com.example.sshomework.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Repository
@Transactional
public interface PersonRepository extends JpaRepository<Person, Long> {
    //Последняя запись
    Person findFirstByOrderByIdDesc();
    //Поиск совпадений по ФИО
    List<Person> findByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);
    //Удаление по ФИО
    void deleteByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);

}
