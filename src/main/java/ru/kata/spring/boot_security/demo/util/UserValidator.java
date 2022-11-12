package ru.kata.spring.boot_security.demo.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceJpa;

import java.util.Optional;

@Component
public class UserValidator implements Validator {

    private final UserServiceJpa userService;

    @Autowired
    public UserValidator(UserServiceJpa userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        Optional<User> tableUser = userService.getUserByUsername(user.getUsername());

        if (tableUser.isPresent()) {
            errors.rejectValue("username", "", "Person with this name is already present");
        }
    }
}
