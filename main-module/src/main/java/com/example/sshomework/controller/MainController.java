package com.example.sshomework.controller;

import com.example.sshomework.dto.book.BookStatusDto;
import com.example.sshomework.service.book.BookService;
import com.example.sshomework.service.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Controller
@RequestMapping(value = "/")
public class MainController {

    private  final BookService bookService;
    private final PersonService personService;

    @Autowired
    public MainController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping
    public String getMainPage(Authentication authentication, Model model) {
        if (authentication != null) {
           model.addAttribute("person", personService.getListUserBooks());
        }
        List<BookStatusDto> books = bookService.getBooks();
        model.addAttribute("books", books);
        return "index";
    }
}
