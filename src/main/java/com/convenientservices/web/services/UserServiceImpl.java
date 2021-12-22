package com.convenientservices.web.services;

import com.convenientservices.web.dto.UserDTO;
import com.convenientservices.web.entities.PointOfServices;
import com.convenientservices.web.entities.Role;
import com.convenientservices.web.entities.User;
import com.convenientservices.web.enums.UserActivationState;
import com.convenientservices.web.mapper.UserMapper;
import com.convenientservices.web.repositories.PointOfServicesRepository;
import com.convenientservices.web.repositories.RoleRepository;
import com.convenientservices.web.repositories.UserRepository;
import com.convenientservices.web.utilities.Utils;
import com.convenientservices.web.utilities.spec.MasterSpec;
import com.convenientservices.web.utilities.spec.PosSpec;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoUnit;
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
    private final PointOfServicesRepository posRepository;
    private final ServiceService serviceService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final MailSenderService mailSenderService;
    private final UserMapper mapper = UserMapper.MAPPER;

    @Value("${user.activation.timelimit}")
    private final Duration userActivationTimeLimit = Duration.of(20, ChronoUnit.SECONDS);

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
        user.setRegistrationDateTime(Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(user);
        mailSenderService.sendActivateCode(user);
        return SUCCESS;
    }

    @Override
    @Transactional
    public UserActivationState activateUser(String activateCode) {
        if (activateCode == null || activateCode.isEmpty()) {
            return UserActivationState.INVALID_CODE;
        }
        User user = userRepository.findFirstByActivationCode(activateCode);

        if (user == null) {
            return UserActivationState.INVALID_CODE;
        } else {
            Duration duration = Duration.between(LocalDateTime.ofInstant(user
                    .getRegistrationDateTime()
                    .toInstant(), ZoneId.systemDefault()), LocalDateTime.now());
            boolean activationTimeNotPassed = duration.minus(userActivationTimeLimit).isNegative();
            logger.info("Registration time = " + LocalDateTime.ofInstant(user.getRegistrationDateTime().toInstant(), ZoneId.systemDefault()));

            logger.info("Activation time not passed = " + activationTimeNotPassed + " time left = " + duration);
            if (!activationTimeNotPassed) return UserActivationState.EXPIRED;
        }
        user.setActivated(true);
        user.setActivationCode(null);
        userRepository.save(user);

        return UserActivationState.ACTIVATED;
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

    @Override
    public List<User> findAll(Map<String, String> params) {
        final Specification<User> specification = params.entrySet().stream()
                .filter(it -> StringUtils.hasText(it.getValue()))
                .map(it -> {
                    if ("name".equals(it.getKey())) {
                        return MasterSpec.nameLike(it.getValue());
                    }

                    if ("service".equals(it.getKey())) {
                        return MasterSpec.serviceLike(it.getValue());
                    }
                    if ("role".equals(it.getKey())) {
                        return MasterSpec.roleEqual(it.getValue());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .reduce(Specification::and)
                .orElse(null);
        return userRepository.findAll(specification);
    }









    @Override
    @Transactional
    public void addMasterToPos(Long posId, Long masterId) {
        Optional<PointOfServices> pos = posRepository.findById(posId);
        Optional<User> master = userRepository.findById(masterId);
        if(pos.isEmpty() || master.isEmpty()) {
            return;
        }
        master.get().getMasterPos().add(pos.get());
    }

    @Override
    public User getById(Long masterId) {
        return userRepository.findById(masterId).orElse(null);
    }

    @Override
    @Transactional
    public User getUserByEmail(String email) {
        Optional<User> userOpt = userRepository.findUserByEmail(email);
        User user = userOpt.orElse(null);
        if (user != null) {
            user.setChangeCode(Utils.getRandomActivationCode());
        }
        mailSenderService.sendRestoreCode(user);
        return user;
    }

    @Override
    @Transactional
    public User getUserByCode(String changeCode, String password) {
        User user = userRepository.findUserByChangeCode(changeCode).orElse(null);
        System.out.println(user);
        if (user != null) {
            user.setPassword(encoder.encode(password));
            user.setChangeCode(null);
        }
        return user;
    }
}
