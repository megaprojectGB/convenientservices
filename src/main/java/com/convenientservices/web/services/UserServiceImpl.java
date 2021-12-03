package com.convenientservices.web.services;

import com.convenientservices.web.dto.UserDTO;
import com.convenientservices.web.entities.PointOfServices;
import com.convenientservices.web.entities.User;
import com.convenientservices.web.mapper.UserMapper;
import com.convenientservices.web.repositories.RoleRepository;
import com.convenientservices.web.repositories.UserRepository;
import com.convenientservices.web.utilities.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final String PASSWORD_DOES_NOT_MATCH = "password";
    private final String SUCCESS = "success";
    private final String USER_EXIST = "user";
    private final String PHONE_EXIST = "phone";
    private final String EMAIL_EXIST = "email";
    private final String UNKNOWN = "Unknown User";
    private final UserRepository userRepository;
    private final ServiceService serviceService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final MailSenderService mailSenderService;
    private final UserMapper mapper = UserMapper.MAPPER;

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
        if (principal == null) {
            return UNKNOWN;
        }
        User user = userRepository.findUserByUserName(principal.getName()).orElse(null);
        if (user == null) {
            return UNKNOWN;
        }
        return "Привет, ".concat(user.getFirstName()).concat(" ").concat(user.getLastName());
    }

    @Override
    public UserDTO getUserDTOByUserName(Principal principal) {
        if (principal == null) {
            throw new NullPointerException("Principal is NULL");
        }
        Optional<User> optionalUser = userRepository.findUserByUserName(principal.getName());
        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException("User nor found");
        }
        return mapper.fromUser(optionalUser.get());
    }

    @Override
    public List<PointOfServices> getFavourites(Principal principal) {
        Optional<User> user = userRepository.findUserByUserName(principal.getName());
        if (user.isEmpty()) {
            return null;
        }
        return user.get().getFavoriteCompanies();
    }

    @Override
    public void deleteServiceByUser(Principal principal, Long id) {
        Optional<User> optional = userRepository.findUserByUserName(principal.getName());
        if (optional.isEmpty()) {
            return;
        }
        User user = optional.get();
        Optional<com.convenientservices.web.entities.Service> service = serviceService.findById(id);
        if (service.isEmpty()) {
            return;
        }
        user.getMasterServices().remove(service.get());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void addServiceToUser(Principal principal, Long id) {
        Optional<User> userOptional = userRepository.findUserByUserName(principal.getName());
        if (userOptional.isEmpty()) {
            return;
        }
        Optional<com.convenientservices.web.entities.Service> serviceOptional = serviceService.findById(id);
        if (serviceOptional.isEmpty()) {
            return;
        }

        if (userOptional.get().getMasterServices().contains(serviceOptional.get())) {
            return;
        }

        userOptional.get().getMasterServices().add(serviceOptional.get());
    }

    @Override
    @Transactional
    public String saveEditUser(Principal principal,
                               User user,
                               Long role,
                               String matchingPassword) {
        User oldUser = userRepository.findUserByUserName(principal.getName()).get();
        System.out.println("first oldUser - " + oldUser.toString());
        if (role == null) {
            System.out.println(role + " is null");
//            role =  oldUser.getRoles().iterator().next().getId();
        } else {
            oldUser.setRoles(new ArrayList<>(Collections.singleton(roleRepository.findById(role).orElse(null))));
        }
        System.out.println("String role is - " + role);
        if (user.getFirstName() == ""){

        } else {
            oldUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() == ""){

        } else {
            oldUser.setLastName(user.getLastName());
        }
        if (matchingPassword == "") {
            if (user.getPassword() == null) {
            } else {

                if (!Utils.passwordMatching(user.getPassword(), matchingPassword)) {
                    return PASSWORD_DOES_NOT_MATCH;
                } else {
                    oldUser.setPassword(encoder.encode(user.getPassword()));
                }
            }
        }
        if(user.getEmail() == "") {

        } else {
            if (userRepository.findFirstByEmail(user.getEmail()).isPresent()) {
                return EMAIL_EXIST;
            } else {
                oldUser.setEmail(user.getEmail());
            }
        }
        if (user.getPhone() == "") {
        }else{
            if (userRepository.findFirstByPhone(user.getPhone()).isPresent()) {
                System.out.println("phone is present");
                return PHONE_EXIST;
            } else {
                oldUser.setPhone(user.getPhone());
            }
        }
//        if (oldUser.getRoles().equals(new ArrayList<>(Collections.singleton(roleRepository.findById(Long.parseLong(role)).orElse(null))))){
//
//        } else {
//            oldUser.setRoles(new ArrayList<>(Collections.singleton(roleRepository.findById(Long.parseLong(role)).orElse(null))));
//        }
//        if (role == null) {
//        } else {
//        user.setRoles(new ArrayList<>(Collections.singleton(roleRepository.findById(role).orElse(null))));
//            System.out.println(role + " is null");
//            role = oldUser.getRoles().iterator().next().getName();
//        }
//        user.setRoles(new ArrayList<>(Collections.singleton(roleRepository.findById(Long.parseLong(role)).orElse(null))));
//        oldUser.setRoles(new ArrayList<>(Collections.singleton(roleRepository.findById(role).orElse(null))));
//        user.setPassword(encoder.encode(user.getPassword()));
        System.out.println("pre save oldUser - " + oldUser.toString());
        userRepository.save(oldUser);
        System.out.println("=========================================");
        return SUCCESS;
    }
}
