package com.convenientservices.web.services;

import com.convenientservices.web.entities.Service;

import java.util.List;

public interface ServiceService {
    Service findById (Long id) throws Exception;
    List<Service> findAll();
    Service save(Service service);
    Service findByName (String name) throws Exception;
}
