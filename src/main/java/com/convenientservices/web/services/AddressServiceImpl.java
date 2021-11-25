package com.convenientservices.web.services;

import com.convenientservices.web.entities.Address;
import com.convenientservices.web.repositories.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {
    private AddressRepository repository;

    @Override
    public Address findById (Long id) throws Exception {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public List<Address> findAll () {
        return repository.findAll();
    }

    @Override
    public Address save (Address address) {
        return repository.save(address);
    }

    @Override
    public Address findByAddress1 (String address) {
        return repository.findByAddress1(address).orElseThrow();
    }

    @Override
    public Address findByAddress2 (String address) {
        return repository.findByAddress2(address).orElseThrow();
    }
}
