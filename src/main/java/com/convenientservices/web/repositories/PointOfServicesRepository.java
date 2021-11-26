package com.convenientservices.web.repositories;

import com.convenientservices.web.entities.PointOfServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointOfServicesRepository extends JpaRepository<PointOfServices, Long> {
    Optional<PointOfServices> findByName(String name);
    List<PointOfServices> findByCategoryNameLikeAndAddress_CityName(String categoryPattern, String city);
    List<PointOfServices> findByCategoryNameLike(String categoryPattern);
}
