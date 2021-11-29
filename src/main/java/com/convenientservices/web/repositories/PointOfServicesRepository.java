package com.convenientservices.web.repositories;

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
    List<PointOfServices> findByCategoryNameLikeAndAddress_CityName(String categoryPattern, String city);
    List<PointOfServices> findByCategoryNameLike(String categoryPattern);
    List<PointOfServices> findAllByBoss(User user);
}
