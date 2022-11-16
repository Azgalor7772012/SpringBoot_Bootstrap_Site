package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import ru.kata.spring.boot_security.demo.util.UserValidator;
import java.security.Principal;


@Controller
public class UserController {

    private final UserValidator userValidator;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserValidator userValidator, UserServiceImpl userServiceImpl) {
        this.userValidator = userValidator;
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/user")
    public String userInfo(Principal principal, Model model) {

        User user = userServiceImpl.getUserByUsername(principal.getName()).get();
        model.addAttribute("loggedUser", user);

        return "user";
    }

}
