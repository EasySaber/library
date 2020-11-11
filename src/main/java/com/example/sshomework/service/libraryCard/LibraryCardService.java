package com.example.sshomework.service.libraryCard;

import com.example.sshomework.dto.LibraryCard;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
public interface LibraryCardService {
    List<LibraryCard> getAll();
    void addNewRecord(LibraryCard libraryCard);
}
