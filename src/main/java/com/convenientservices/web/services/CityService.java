package com.convenientservices.web.services;

import com.convenientservices.web.exceptions.CityNotFoundException;
import com.convenientservices.web.entities.City;

import java.util.List;

public interface CityService {
    City findById (Integer id) throws Exception;
    List<City> findAll();
    City save(City city);
    City findByName(String name) throws CityNotFoundException;
    String findCorrectNameOfCity(String name);
}
