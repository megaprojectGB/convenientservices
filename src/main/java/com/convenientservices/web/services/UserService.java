package com.convenientservices.web.services;

import com.convenientservices.web.entities.User;

public interface UserService {
    User getUserByUsername(String name);
    void registerNewUser(User user, String role, String password);
    boolean activateUser(String activateCode);
}
