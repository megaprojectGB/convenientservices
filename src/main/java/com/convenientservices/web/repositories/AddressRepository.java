package com.convenientservices.web.repositories;

import com.convenientservices.web.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByAddress1 (String address);
    Optional<Address> findByAddress2 (String address);
}
