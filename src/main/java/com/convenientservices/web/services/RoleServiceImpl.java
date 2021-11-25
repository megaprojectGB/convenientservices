package com.convenientservices.web.services;

import com.convenientservices.web.entities.Role;
import com.convenientservices.web.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private RoleRepository repository;

    @Override
    public Role findById (Long id) throws Exception {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public List<Role> findAll () {
        return repository.findAll();
    }

    @Override
    public Role save (Role role) {
        return repository.save(role);
    }
}
