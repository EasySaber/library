package com.example.sshomework.controller;

import com.example.sshomework.dto.user.UserDto;
import com.example.sshomework.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping(value = "")
    public String getUsers(Model model) {
        List<UserDto> users = userService.getAll();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping(value = "/registration")
    public String addUser(Model model) {
        UserDto newUser = new UserDto();
        model.addAttribute("newUser", newUser);
        return "admin/registration";
    }

    @PostMapping(value = "/registration")
    public String saveUser(
            @ModelAttribute(name = "newUser")
            @Valid UserDto user,
            BindingResult validResult)
    {
        if (validResult.hasErrors()) {
            return "admin/registration";
        }
        if (!userService.userUnique(user.getUsername())) {
            //Пользователь с таким именем уже существует
            validResult.rejectValue("username", "badUniqueUser");
            return "admin/registration";
        }
        System.out.println();
        userService.addNewUser(user);
        return "redirect:/admin/";
    }

}
