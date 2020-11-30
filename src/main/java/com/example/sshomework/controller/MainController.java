package com.example.sshomework.controller;

import com.example.sshomework.dto.book.BookStatusDto;
import com.example.sshomework.service.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/")
public class MainController {

    private final BookService bookService;

    @GetMapping
    public String getMainPage(Model model) {
        List<BookStatusDto> books = bookService.getBooks();
        model.addAttribute("books", books);
        return "index";
    }
}
