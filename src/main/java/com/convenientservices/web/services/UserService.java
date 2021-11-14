package com.convenientservices.web.services;

import com.convenientservices.web.dto.UserDTO;
import com.convenientservices.web.entities.User;

import java.security.Principal;

public interface UserService {
    User getUserByUsername(String name);

    String registerNewUser(User user, String role, String password);

    boolean activateUser(String activateCode);

    String getFIO(Principal principal);

    UserDTO getUserDTOByUserName(Principal principal);
}
