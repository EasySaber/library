package com.example.sshomework.repository.history;

import com.example.sshomework.entity.history.GenreHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Repository
public interface GenreHistoryRepository extends JpaRepository<GenreHistory, Long> {
    List<GenreHistory> findByEntityId(Long entityId);
}
