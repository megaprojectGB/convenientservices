package com.convenientservices.web.services;

import com.convenientservices.web.entities.PointOfServices;
import com.convenientservices.web.entities.User;
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
    private final PointOfServicesRepository posRepository;
    private final UserRepository userRepository;

    @Override
    public PointOfServices findById (Long id) throws Exception {
        return posRepository.findById(id).orElseThrow();
    }

    @Override
    public List<PointOfServices> findAll () {
        return posRepository.findAll();
    }

    @Override
    public PointOfServices save (PointOfServices pointOfServices) {
        return posRepository.save(pointOfServices);
    }

    @Override
    public PointOfServices findByName (String name) {
        return posRepository.findByName(name).orElseThrow();
    }

    @Override
    public List<PointOfServices> findAllByCity (String city) {
        return posRepository.findAll().stream().filter(t -> t.getAddress().getCity().getName().equals(city)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteFavouriteCompanyByUser(Principal principal, Long id) {
        Optional<User> userOptional = userRepository.findUserByUserName(principal.getName());
        if (userOptional.isEmpty()) {
            return;
        }
        Optional<PointOfServices> pointOfServices = posRepository.findById(id);
        if (pointOfServices.isEmpty()) {
            return;
        }

        userOptional.get().getFavoriteCompanies().remove(pointOfServices.get());
    }

    @Override
    @Transactional
    public void addFavouriteCompanyByUser(Principal principal, Long id) {
        Optional<User> userOptional = userRepository.findUserByUserName(principal.getName());
        if (userOptional.isEmpty()) {
            return;
        }
        Optional<PointOfServices> pointOfServices = posRepository.findById(id);
        if (pointOfServices.isEmpty()) {
            return;
        }

        userOptional.get().getFavoriteCompanies().add(posRepository.findById(id).get());

    }
}

