package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    private final UserValidator userValidator;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserValidator userValidator, UserServiceImpl userServiceImpl) {
        this.userValidator = userValidator;
        this.userServiceImpl = userServiceImpl;
    }


    @GetMapping("/registration")
    public String toRegister(@ModelAttribute("user") User user) {

        return "registration";
    }


    @GetMapping("/login")
    public String toLogin() {
        return "login";
    }


    @PostMapping("/registration")
    public String postRegister(@ModelAttribute("user")
                                   @Valid User user,
                               BindingResult bindingResult,
                               HttpServletRequest request) {

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        List<String> roles = List.of(request.getParameterValues("chosen_roles"));
        userServiceImpl.register(user, roles);
        return "redirect:/hello";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
    @GetMapping("/index2")
    public String index2() {
        return "index2";
    }



}
