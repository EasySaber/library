package com.example.sshomework.repository;

import com.example.sshomework.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> , JpaSpecificationExecutor<Book> {
    List<Book> findBookByAuthorBookId(Long id);
    List<Book> findByAuthorBookId(Long id);
    //Последняя запись
    Book findFirstByOrderByIdDesc();
}
