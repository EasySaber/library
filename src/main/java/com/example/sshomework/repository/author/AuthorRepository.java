package com.example.sshomework.repository.author;

import com.example.sshomework.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Aleksey Romodin
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, CustomAuthorRepository {
    //Последняя запись
    Author findFirstByOrderByIdDesc();
}
