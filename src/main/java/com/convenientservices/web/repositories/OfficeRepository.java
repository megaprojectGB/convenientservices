package com.convenientservices.web.repositories;

import com.convenientservices.web.entities.Office;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeRepository extends JpaRepository<Office, Long> {
}
