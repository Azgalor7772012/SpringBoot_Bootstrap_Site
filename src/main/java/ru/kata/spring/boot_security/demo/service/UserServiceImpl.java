package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String email) {
       return userRepository.findByEmail(email);
    }


    @Transactional
    public void register(User user, List<String> roles) {

        StringBuilder stringOfRoles = new StringBuilder();

        for (String role : roles) {
            user.addRoleToUser(new Role(role));
            stringOfRoles.append(role.substring(5)).append(" ");
        }

        user.setRole(stringOfRoles.toString());


        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Transactional
    public List<User> showAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Deprecated
    public Optional<User> showOneUser(Long id) {
       return userRepository.findById(id);
    }

    @Transactional
    public void update(User user, List<String> roles) {
        Long id = user.getId();
        roleRepository.deleteAllByUserId(id);

        StringBuilder stringOfRoles = new StringBuilder();

        for (String role : roles) {
            user.addRoleToUser(new Role(role));
            stringOfRoles.append(role.substring(5)).append(" ");
        }

        user.setRole(stringOfRoles.toString());


        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
