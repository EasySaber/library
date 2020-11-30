package com.example.sshomework.repository.book;

import com.example.sshomework.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

/**
 * @author Aleksey Romodin
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> , JpaSpecificationExecutor<Book>, CustomBookRepository {
    //Последняя запись
    Book findFirstByOrderByIdDesc();
}
