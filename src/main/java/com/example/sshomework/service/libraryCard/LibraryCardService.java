package com.example.sshomework.service.libraryCard;

import com.example.sshomework.dto.LibraryCardDto;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
public interface LibraryCardService {
    List<LibraryCardDto> getDebtors();
    LibraryCardDto prolongation(Long personId, Long bookId, Long days);
    LibraryCardDto addNewCard(Long personId, Long bookId);
}
