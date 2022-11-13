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
public class UserServiceJpa {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceJpa(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String name) {
       return userRepository.findByUsername(name);
    }


    @Transactional
    public void register(User user, List<String> roles) {
        for (String role : roles) {
            user.addRoleToUser(new Role(role));
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Transactional
    public List<User> showAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public Optional<User> showOneUser(Long id) {
       return userRepository.findById(id);
    }

    @Transactional
    public void update(User user, List<String> roles) {
        Long userId = user.getId();

        roleRepository.deleteAllByUserId(userId);

        for (String role : roles) {
            user.addRoleToUser(new Role(role));
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }


}
