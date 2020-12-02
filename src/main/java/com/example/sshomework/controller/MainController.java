package com.example.sshomework.controller;

import com.example.sshomework.dto.PersonDto;
import com.example.sshomework.dto.book.BookStatusDto;
import com.example.sshomework.service.book.BookService;
import com.example.sshomework.service.person.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.extras.springsecurity5.util.SpringSecurityContextUtils;

import java.security.Principal;
import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/")
public class MainController {

    private final BookService bookService;
    private final PersonService personService;

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
