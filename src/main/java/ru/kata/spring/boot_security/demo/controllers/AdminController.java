package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class AdminController {

    private final UserValidator userValidator;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public AdminController(UserValidator userValidator, UserServiceImpl userServiceImpl) {
        this.userValidator = userValidator;
        this.userServiceImpl = userServiceImpl;
    }


    @PostMapping("/createNewUser")
    public String postCreateNewUser(@ModelAttribute("newUser") @Valid User user,
                                    BindingResult bindingResult, @RequestParam(value = "role", required = false) List<String> roles) {
        userValidator.validate(user, bindingResult);

        if(bindingResult.hasErrors()) {
            return "admin";
        }

        userServiceImpl.register(user, roles);

        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String adminPage(Model model, Principal principal, @ModelAttribute("newUser") User user) {
        model.addAttribute("users", userServiceImpl.showAllUsers());
        if (userServiceImpl.getUserByUsername(principal.getName()).isEmpty()) {
            return "redirect:/login";
        }

        //Чтобы писать роли вверху страницы
        User loggedUser = userServiceImpl.getUserByUsername(principal.getName()).get();
        model.addAttribute("loggedUser", loggedUser);

        return "admin";
    }


    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userServiceImpl.delete(id);
        return "redirect:/admin";
    }



    @PatchMapping("/edit/{id}")
    public String patchEditUser(@ModelAttribute("newUser") @Valid User user,BindingResult bindingResult,
                                @RequestParam("role") List<String> roles) {

//        userValidator.validate(user, bindingResult);
//        if(bindingResult.hasErrors()) {
//            return "admin";
//        }

        userServiceImpl.update(user, roles);
        return "redirect:/admin";
    }
}
