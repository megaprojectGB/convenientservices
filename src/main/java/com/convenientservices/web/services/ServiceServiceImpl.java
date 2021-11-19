package com.convenientservices.web.services;

import com.convenientservices.web.entities.Service;
import com.convenientservices.web.repositories.ServiceRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private ServiceRepository repository;

    @Override
    public Service findById (Long id) throws Exception {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public List<Service> findAll () {
        return repository.findAll();
    }

    @Override
    public Service save (Service service) {
        return repository.save(service);
    }

    @Override
    public Service findByName (String name) {
        return repository.findByName(name).orElseThrow();
    }
}