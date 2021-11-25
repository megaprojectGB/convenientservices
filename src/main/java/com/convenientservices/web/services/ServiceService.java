package com.convenientservices.web.services;

import com.convenientservices.web.entities.Service;

import java.util.List;
import java.util.Optional;

public interface ServiceService {
    Optional<Service> findById(Long id);

    List<Service> findAll();

    Service save(Service service);

    Optional<Service> findByName(String name);
}
