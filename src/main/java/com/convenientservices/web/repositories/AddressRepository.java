package com.convenientservices.web.repositories;

import com.convenientservices.web.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
