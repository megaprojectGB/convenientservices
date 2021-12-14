package com.convenientservices.web.services;

import com.convenientservices.web.dto.UserDTO;
import com.convenientservices.web.entities.PointOfServices;
import com.convenientservices.web.entities.User;
import com.convenientservices.web.enums.UserActivationState;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;
import java.util.List;

public interface UserService extends UserDetailsService {
    UserDetails loadUserByUsername(String username);

    User getUserByUsername(String name);

    String registerNewUser(User user, String role, String password);

    UserActivationState activateUser(String activateCode);

    String getFIO(Principal principal);

    UserDTO getUserDTOByUserName(Principal principal);

    List<PointOfServices> getFavourites(Principal principal);

    void deleteServiceByUser(Principal principal, Long id);

    void addServiceToUser(Principal principal, Long id);

    String saveEditUser(Principal principal, UserDTO user, String matchingPassword, String password);

    List<User> getAllMasters();

    void addMasterToPos(Long posId, Long masterId);

    User getById(Long masterId);

    User getUserByEmail(String email);

    User getUserByCode(String changeCode, String password);
}
