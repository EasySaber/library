package com.example.sshomework.repository.author;

import com.example.sshomework.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Aleksey Romodin
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, CustomAuthorRepository {
    //Последняя запись
    Optional<Author> findFirstByOrderByIdDesc();
}
