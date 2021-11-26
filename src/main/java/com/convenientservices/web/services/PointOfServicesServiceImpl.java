package com.convenientservices.web.services;

import com.convenientservices.web.Exceptions.RecordNotFoundException;
import com.convenientservices.web.entities.City;
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
        if (posRepository.findById(id).isEmpty()) throw new RecordNotFoundException("No pointOfServices found with id = " + id);
        return posRepository.findById(id).get();
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
    public PointOfServices findByName (String name) throws RecordNotFoundException {
        Optional<PointOfServices> pointOfServices = posRepository.findByName(name);
        if (pointOfServices.isEmpty()) throw new RecordNotFoundException("No pointOfService found with name " + name);
        return pointOfServices.get();
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

    @Override
    public List<PointOfServices> findByCategoryLike(String categoryPattern) throws RecordNotFoundException {
        List<PointOfServices> pointsOfServices = posRepository.findByCategoryNameLike(categoryPattern);
        if (pointsOfServices.isEmpty()) throw new RecordNotFoundException("No records found for category " + categoryPattern);
        return pointsOfServices;
    }

    @Override
    public List<PointOfServices> findByCategoryLikeAndCity(String categoryPattern, City city) throws RecordNotFoundException {
        List<PointOfServices> pointsOfServices = posRepository.findByCategoryNameLikeAndAddress_CityName(categoryPattern, city.getName());
        if (pointsOfServices.isEmpty()) throw new RecordNotFoundException("No records found for category " + categoryPattern + " and city " + city.getName());
        return pointsOfServices;
    }
}

