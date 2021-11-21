package com.convenientservices.web.services;

import com.convenientservices.web.dto.UserDTO;
import com.convenientservices.web.entities.PointOfServices;
import com.convenientservices.web.entities.User;

import java.security.Principal;
import java.util.List;

public interface UserService {
    User getUserByUsername(String name);

    String registerNewUser(User user, String role, String password);

    boolean activateUser(String activateCode);

    String getFIO(Principal principal);

    UserDTO getUserDTOByUserName(Principal principal);

    List<PointOfServices> getFavourites(Principal principal);

    void deleteServiceByUser(Principal principal, Long id);

    void addServiceToUser(Principal principal, Long id);
}
