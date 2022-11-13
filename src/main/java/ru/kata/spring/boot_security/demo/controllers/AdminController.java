package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceJpa;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class AdminController {

    private final UserValidator userValidator;
    private final UserServiceJpa userServiceJpa;

    @Autowired
    public AdminController(UserValidator userValidator, UserServiceJpa userServiceJpa) {
        this.userValidator = userValidator;
        this.userServiceJpa = userServiceJpa;
    }

    @GetMapping("/createNewUser")
    public String getCreateNewUser(@ModelAttribute("user") User user) {
        return "addNewUserByAdmin";
    }

    @PostMapping("/createNewUser")
    public String postCreateNewUser(@ModelAttribute("user") @Valid User user,
                                    BindingResult bindingResult, @RequestParam(value = "chosen_roles", required = false) List<String> roles) {
        userValidator.validate(user, bindingResult);

        if(bindingResult.hasErrors()) {
            return "addNewUserByAdmin";
        }

        userServiceJpa.register(user, roles);

        return "redirect:/admin";
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

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userServiceJpa.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String getEditUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userServiceJpa.showOneUser(id).get());
        return "edit";
    }

    @PatchMapping("/edit/{id}")
    public String patchEditUser(@ModelAttribute("user") @Valid User user,BindingResult bindingResult,
                                HttpServletRequest request) {

        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors()) {
            return "edit";
        }

        List<String> roles = List.of(request.getParameterValues("chosen_roles"));

        userServiceJpa.update(user, roles);
        return "redirect:/admin";
    }
}
