package com.convenientservices.web.services;

import com.convenientservices.web.entities.ServiceCategory;
import com.convenientservices.web.repositories.ServiceCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceCategoryServiceImpl implements ServiceCategoryService {
    private ServiceCategoryRepository serviceCategoryRepository;

    @Override
    public ServiceCategory findById (Long id) throws Exception {
        return serviceCategoryRepository.findById(id).orElseThrow();
    }

    @Override
    public List<ServiceCategory> findAll () {
        return serviceCategoryRepository.findAll();
    }

    @Override
    public ServiceCategory save (ServiceCategory serviceCategory) {
        return serviceCategoryRepository.save(serviceCategory);
    }

    @Override
    public ServiceCategory findByName (String name) {
        return serviceCategoryRepository.findByName(name).orElseThrow();
    }
}
