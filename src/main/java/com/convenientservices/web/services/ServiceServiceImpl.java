package com.convenientservices.web.services;

import com.convenientservices.web.dto.ServiceDto;
import com.convenientservices.web.entities.Service;
import com.convenientservices.web.entities.ServiceCategory;
import com.convenientservices.web.mapper.ServiceMapper;
import com.convenientservices.web.repositories.ServiceCategoryRepository;
import com.convenientservices.web.repositories.ServiceRepository;
import com.convenientservices.web.utilities.Utils;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private ServiceRepository serviceRepository;
    private ServiceCategoryRepository serviceCategoryRepository;
    private final ServiceMapper mapperService = ServiceMapper.MAPPER;

    @Override
    public Optional<Service> findById(Long id) {
        return serviceRepository.findById(id);
    }

    @Override
    public List<ServiceDto> findAll() {
        return mapperService.fromServiceList(serviceRepository.findAll());
    }

    @Override
    public Service save(Service service) {
        return serviceRepository.save(service);
    }

    @Override
    public Optional<Service> findByName(String name) {
        return serviceRepository.findByName(name);
    }

    @Override
    public void createNewCategoryService(String name, String duration, String categoryId) {
        Service service = new Service();
        try {
            ServiceCategory category = serviceCategoryRepository.findById(Long.parseLong(categoryId)).orElse(null);
            service.setName(name);
            service.setDuration(Utils.changeMinuteToSeconds(Long.parseLong(duration)));
            service.setCategory(category);
            serviceRepository.save(service);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
