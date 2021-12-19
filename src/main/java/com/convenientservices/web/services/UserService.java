package com.convenientservices.web.services;

import com.convenientservices.web.dto.UserDTO;
import com.convenientservices.web.entities.PointOfServices;
import com.convenientservices.web.entities.User;
import com.convenientservices.web.enums.UserActivationState;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UserService {
    User getUserByUsername(String name);

    String registerNewUser(User user, String role, String password);

    UserActivationState activateUser(String activateCode);

    String getFIO(Principal principal);

    UserDTO getUserDTOByUserName(Principal principal);

    List<PointOfServices> getFavourites(Principal principal);

    void deleteServiceByUser(Principal principal, Long id);

    void addServiceToUser(Principal principal, Long id);

    String saveEditUser(Principal principal, UserDTO user, String matchingPassword, String password);
    List<User> findAll(Map<String, String> param);

    List<User> getAllMasters();

    void addMasterToPos(Long posId, Long masterId);

    User getById(Long masterId);

    User getUserByEmail(String email);

    User getUserByCode(String changeCode, String password);
}
