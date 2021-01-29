package com.example.sshomework.repository.history;

import com.example.sshomework.entity.history.AuthorHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Repository
public interface AuthorHistoryRepository extends JpaRepository<AuthorHistory, Long> {
    List<AuthorHistory> findByEntityId(Long entityId);
}
