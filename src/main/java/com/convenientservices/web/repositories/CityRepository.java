package com.convenientservices.web.repositories;

import com.convenientservices.web.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByName(String name);
    Optional<City> findByNameAndState(String name, String state);
}
