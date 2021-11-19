package com.convenientservices.web.services;

import com.convenientservices.web.entities.City;
import com.convenientservices.web.repositories.CityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService {
    private CityRepository repository;

    @Override
    public City findById (Long id) throws Exception {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public List<City> findAll () {
        return repository.findAll();
    }

    @Override
    public City save (City city) {
        return repository.save(city);
    }

    @Override
    public City findByName (String name) {
        return repository.findByName(name).orElseThrow();
    }
}
