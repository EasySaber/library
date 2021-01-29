package com.example.sshomework.repository;

import com.example.sshomework.entity.LibraryCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Aleksey Romodin
 */
@Repository
public interface LibraryCardRepository extends JpaRepository<LibraryCard, Long> {
    Optional<LibraryCard> findByBookIdAndPersonId(Long bookId, Long personId);
    List<LibraryCard> findByPersonId(Long personId);
}
