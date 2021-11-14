package com.convenientservices.web.repositories;

import com.convenientservices.web.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}
