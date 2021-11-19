package com.convenientservices.web.services;
import com.convenientservices.web.entities.Address;
import java.util.List;

public interface AddressService {
    Address findById (Long id) throws Exception;
    List<Address> findAll();
    Address save(Address address);
    Address findByAddress1(String address);
    Address findByAddress2(String address);
}
