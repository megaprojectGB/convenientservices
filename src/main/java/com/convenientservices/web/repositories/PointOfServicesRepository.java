package com.convenientservices.web.repositories;

import com.convenientservices.web.entities.City;
import com.convenientservices.web.entities.PointOfServices;
import com.convenientservices.web.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointOfServicesRepository extends JpaRepository<PointOfServices, Long>, JpaSpecificationExecutor<PointOfServices> {
    Optional<PointOfServices> findByName(String name);
    List<PointOfServices> findByCategoryNameLikeAndAddress_City(String categoryPattern, City city);
    List<PointOfServices> findByCategoryNameAndAddress_City(String categoryName, City city);
    List<PointOfServices> findByCategoryNameLike(String categoryPattern);
    List<PointOfServices> findAllByAddress_City(City city);
    List<PointOfServices> findAllByBoss(User user);
    List<PointOfServices> findAllByNameLikeAndCategoryNameAndAddress_CityName(String posNamePattern, String categoryName, String cityName);
    List<PointOfServices> findAllByNameContainingIgnoreCase(String posNamePattern);
}
