package com.convenientservices.web.services;

import com.convenientservices.web.dto.UserDTO;
import com.convenientservices.web.entities.PointOfServices;
import com.convenientservices.web.entities.Role;
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
import java.util.stream.Collectors;

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
                               UserDTO userDto,
                               String password,
                               String matchingPassword) {
        Optional<User> oldUserOpt = userRepository.findUserByUserName(principal.getName());
        Optional<Role> roleOpt = roleRepository.findByName(userDto.getRole());
        Role role = roleOpt.orElse(null);

        if (oldUserOpt.isEmpty()) {
            return "success";
        }

        if (!Utils.passwordMatching(password, matchingPassword)) {
            return PASSWORD_DOES_NOT_MATCH;
        }

        Optional<User> phoneUser = userRepository.findFirstByPhone(userDto.getPhone());
        if (phoneUser.isPresent() && !principal.getName().equals(phoneUser.get().getUserName())) {
            return PHONE_EXIST;
        }

        Optional<User> emailUser = userRepository.findFirstByEmail(userDto.getEmail());
        if (emailUser.isPresent() && !principal.getName().equals(emailUser.get().getUserName())) {
            return EMAIL_EXIST;
        }

        User user = mapper.toUser(userDto);
        user.setFavoriteCompanies(oldUserOpt.get().getFavoriteCompanies());
        user.setMasterServices(oldUserOpt.get().getMasterServices());
        user.setMasterPos(oldUserOpt.get().getMasterPos());
        user.setRoles(Collections.singleton(role));
        if (password.isEmpty()) {
            user.setPassword(oldUserOpt.get().getPassword());
        } else {
            user.setPassword(encoder.encode(password));
        }
        userRepository.save(user);
        return SUCCESS;
    }

    @Override
    public List<User> getAllMasters() {
        List<User> users = userRepository.findAll();
        Role role = roleRepository.findByName("ROLE_MASTER").orElse(null);
        return users.stream().filter(e -> e.getRoles().contains(role)).collect(Collectors.toList());
    }

}
