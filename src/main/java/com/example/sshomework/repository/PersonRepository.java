package com.example.sshomework.repository;

import com.example.sshomework.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author Aleksey Romodin
 */
@Repository
@Transactional
public interface PersonRepository extends JpaRepository<Person, Long> {
    //Последняя запись
    Optional<Person> findFirstByOrderByIdDesc();
    //Поиск совпадений по ФИО
    List<Person> findByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);
    //Поиск по аккаунту
    Optional<Person> findByAccount_Username(String username);
}
