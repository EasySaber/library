package com.example.sshomework.service.libraryCard;

import com.example.sshomework.dto.LibraryCardDto;
import com.example.sshomework.exception.PersonBookDebtException;

import java.util.List;
import java.util.Optional;

/**
 * @author Aleksey Romodin
 */
public interface LibraryCardService {
    List<LibraryCardDto> getDebtors();
    Optional<LibraryCardDto> prolongation(Long personId, Long bookId, Long days);
    Optional<LibraryCardDto> addNewCard(Long personId, Long bookId) throws PersonBookDebtException;
}
