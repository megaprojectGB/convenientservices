package com.convenientservices.web.services;

import com.convenientservices.web.entities.Service;
import com.convenientservices.web.entities.ServiceCategory;

import java.util.List;

public interface ServiceCategoryService {
    ServiceCategory findById (Long id) throws Exception;
    List<ServiceCategory> findAll();
    ServiceCategory save(ServiceCategory serviceCategory);
    ServiceCategory findByName (String name);
}
