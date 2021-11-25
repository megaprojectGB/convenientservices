package com.convenientservices.web.services;

import com.convenientservices.web.entities.PointOfServices;
import com.convenientservices.web.repositories.PointOfServicesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class PointOfServicesServiceImpl implements PointOfServiceServices {
    private PointOfServicesRepository repository;

    @Override
    public PointOfServices findById (Long id) throws Exception {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public List<PointOfServices> findAll () {
        return repository.findAll();
    }

    @Override
    public PointOfServices save (PointOfServices pointOfServices) {
        return repository.save(pointOfServices);
    }

    @Override
    public PointOfServices findByName (String name) {
        return repository.findByName(name).orElseThrow();
    }

    public List<PointOfServices> findAllByCity (String city) {
       return repository.findAll().stream().filter(t->t.getAddress().getCity().getName().equals(city)).collect(Collectors.toList());
    }
}

