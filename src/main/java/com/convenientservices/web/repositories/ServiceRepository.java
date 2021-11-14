package com.convenientservices.web.repositories;

import com.convenientservices.web.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
