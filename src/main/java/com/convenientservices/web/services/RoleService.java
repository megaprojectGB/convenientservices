package com.convenientservices.web.services;

import com.convenientservices.web.entities.Role;

import java.util.List;

public interface RoleService {
    Role findById (Long id) throws Exception;
    List<Role> findAll();
    Role save(Role role);
}
