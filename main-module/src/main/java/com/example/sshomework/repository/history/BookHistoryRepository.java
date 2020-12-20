package com.example.sshomework.repository.history;

import com.example.sshomework.entity.history.BookHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Repository
public interface BookHistoryRepository extends JpaRepository<BookHistory, Long> {
    List<BookHistory> findByEntityId(Long entityId);
}
