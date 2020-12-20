package com.example.sshomework.repository.history;

import com.example.sshomework.entity.history.PersonHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Repository
public interface PersonHistoryRepository extends JpaRepository<PersonHistory, Long> {
    List<PersonHistory> findByEntityId(Long entityId);
}
