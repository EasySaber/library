package com.example.sshomework.repository;

import com.example.sshomework.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Aleksey Romodin
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    //Последняя запись
    Author findFirstByOrderByIdDesc();
}
