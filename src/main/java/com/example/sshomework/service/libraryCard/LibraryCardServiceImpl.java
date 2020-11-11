package com.example.sshomework.service.libraryCard;

import com.example.sshomework.dto.LibraryCard;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Service
public class LibraryCardServiceImpl implements LibraryCardService {
    private static final List<LibraryCard> libraryCards = new ArrayList<>();

    @Override
    public List<LibraryCard> getAll(){
        return libraryCards;
    }

    @Override
    public void addNewRecord(LibraryCard libraryCard) {
        libraryCards.add(libraryCard);
    }
}
