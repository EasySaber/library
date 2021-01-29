package com.example.feignclient.client;

import com.example.feignclient.dto.LibraryCardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@RequestMapping("/api/libraryCard")
@FeignClient(name = "main-module")
public interface LibraryCardFeignClient {

    //Показать всех задолжников
    @GetMapping("/getDebtors")
    List<LibraryCardDto> getDebtors();

    //Продление сроков
    @PutMapping("/prolongation")
    LibraryCardDto prolongation(
            @RequestParam(name = "personId") Long personId,
            @RequestParam(name = "bookId") Long bookId,
            @RequestParam(name = "days") Long days);

    //Получение новой книги
    @PostMapping("/add")
    ResponseEntity<LibraryCardDto> addNewCard(
            @RequestParam(name = "personId") Long personId,
            @RequestParam(name = "bookId") Long bookId);
}
