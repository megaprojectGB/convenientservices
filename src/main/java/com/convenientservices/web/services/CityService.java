package com.convenientservices.web.services;

import com.convenientservices.web.entities.City;

import java.util.List;

public interface CityService {
    City findById (Long id) throws Exception;
    List<City> findAll();
    City save(City city);
    City findByName(String name);
    String findCorrectNameOfCity(String name);
}
