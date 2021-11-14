package com.convenientservices.web.services;

import com.convenientservices.web.dto.UserDTO;
import com.convenientservices.web.entities.User;
import com.convenientservices.web.mapper.UserMapper;
import com.convenientservices.web.repositories.RoleRepository;
import com.convenientservices.web.repositories.UserRepository;
import com.convenientservices.web.utilities.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final String PASSWORD_DOES_NOT_MATCH = "password";
    private final String SUCCESS = "success";
    private final String USER_EXIST = "user";
    private final String PHONE_EXIST = "phone";
    private final String EMAIL_EXIST = "email";
    private final String UNKNOWN = "Unknown User";
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder encoder;
    private final MailSenderService mailSenderService;
    private final UserMapper mapper = UserMapper.MAPPER;

    public UserServiceImpl(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

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
    public String registerNewUser(User user, String role, String matchingPassword) {
        if (!Utils.passwordMatching(user.getPassword(), matchingPassword)) {
            return PASSWORD_DOES_NOT_MATCH;
        }
        if (userRepository.findUserByUserName(user.getUserName()).isPresent()) {
            return USER_EXIST;
        }
        if (userRepository.findFirstByEmail(user.getEmail()).isPresent()) {
            return EMAIL_EXIST;
        }
        if (userRepository.findFirstByPhone(user.getPhone()).isPresent()) {
            return PHONE_EXIST;
        }
        user.setRoles(new ArrayList<>(Collections.singleton(roleRepository.findById(Long.parseLong(role)).orElse(null))));
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActivationCode(Utils.getRandomActivationCode());
        userRepository.save(user);
        mailSenderService.sendActivateCode(user);
        return SUCCESS;
    }

    @Override
    @Transactional
    public boolean activateUser(String activateCode) {
        if (activateCode == null || activateCode.isEmpty()) {
            return false;
        }
        User user = userRepository.findFirstByActivationCode(activateCode);
        if (user == null) {
            return false;
        }
        user.setActivated(true);
        user.setActivationCode(null);
        userRepository.save(user);

        return true;
    }

    @Override
    public String getFIO(Principal principal) {
        if (principal == null) return UNKNOWN;
        User user = userRepository.findUserByUserName(principal.getName()).orElse(null);
        return "Привет, ".concat(user.getFirstName()).concat(" ").concat(user.getLastName());
    }

    @Override
    public UserDTO getUserDTOByUserName(Principal principal) {
        if (principal == null) throw new NullPointerException("Principal is NULL");
        Optional<User> optionalUser = userRepository.findUserByUserName(principal.getName());
        if (optionalUser.isEmpty()) throw new NoSuchElementException("User nor found");
        return mapper.fromUser(optionalUser.get());
    }
}
