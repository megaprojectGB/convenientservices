package com.convenientservices.web.services;

import com.convenientservices.web.entities.User;
import com.convenientservices.web.repositories.RoleRepository;
import com.convenientservices.web.repositories.UserRepository;
import com.convenientservices.web.utilities.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder encoder;

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserByUsername(String name) {
        Optional<User> userOptional = userRepository.findUserByUserName(name);
        if (userOptional.isEmpty()) {
            // TODO: 23.10.2021 Сделать что-то логичное а не исключение
            throw new NoSuchElementException("User not found");
        }
        return userOptional.get();
    }

    @Override
    public void registerNewUser(User user, String role, String matchingPassword) {
        if (!Utils.passwordMatching(user.getPassword(), matchingPassword)) {
            throw new IllegalArgumentException("The password doesn't match");
        }
        user.setRoles(new ArrayList<>(Collections.singleton(roleRepository.findById(Long.parseLong(role)).orElse(null))));
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActivationCode(Utils.getRandomActivationCode());
        userRepository.save(user);
    }
}
