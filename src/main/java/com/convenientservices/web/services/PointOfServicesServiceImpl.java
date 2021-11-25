package com.convenientservices.web.services;

import com.convenientservices.web.entities.PointOfServices;
import com.convenientservices.web.entities.User;
import com.convenientservices.web.repositories.CityRepository;
import com.convenientservices.web.repositories.PointOfServicesRepository;
import com.convenientservices.web.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PointOfServicesServiceImpl implements PointOfServiceServices {
    private final PointOfServicesRepository repository;
    private final UserRepository userRepository;
    private final PointOfServicesRepository pos;

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

    @Override
    public List<PointOfServices> findAllByCity (String city) {
        return repository.findAll().stream().filter(t -> t.getAddress().getCity().getName().equals(city)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteFavouriteCompanyByUser(Principal principal, Long id) {
        Optional<User> userOptional = userRepository.findUserByUserName(principal.getName());
        if (userOptional.isEmpty()) {
            return;
        }
        Optional<PointOfServices> pointOfServices = pos.findById(id);
        if (pointOfServices.isEmpty()) {
            return;
        }

        userOptional.get().getFavoriteCompanies().remove(pointOfServices.get());
    }
}

