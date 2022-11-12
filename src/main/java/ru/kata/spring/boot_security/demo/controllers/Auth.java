package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceJpa;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class Auth {

    private final UserValidator userValidator;
    private final UserServiceJpa userServiceJpa;

    @Autowired
    public Auth(UserValidator userValidator, UserServiceJpa userServiceJpa) {
        this.userValidator = userValidator;
        this.userServiceJpa = userServiceJpa;
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
                               BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userServiceJpa.register(user);
        return "redirect:/hello";
    }


    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("users", userServiceJpa.showAllUsers());
        return "admin";
    }

    @GetMapping("/user")
    public String userInfo(Principal principal, Model model) {
        User user = userServiceJpa.getUserByUsername(principal.getName()).get();

        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/user/{id}")
    public String currentUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userServiceJpa.showOneUser(id).get());
        return "userInfo";
    }

    @DeleteMapping("/delete")
    public String deleteUser(@ModelAttribute("user") User user) {
        userServiceJpa.delete(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String getEditUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        return "edit";
    }

    @PatchMapping("/edit/{id}")
    public String patchEditUser(@PathVariable("id") Long id,@ModelAttribute("user") User user) {
        userServiceJpa.update(userServiceJpa.showOneUser(id).get());
        return "redirect:/admin";
    }
}
