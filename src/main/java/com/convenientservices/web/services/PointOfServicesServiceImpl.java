package com.convenientservices.web.services;

import com.convenientservices.web.dto.PointOfServiceDto;
import com.convenientservices.web.entities.*;
import com.convenientservices.web.exceptions.RecordNotFoundException;
import com.convenientservices.web.repositories.*;
import com.convenientservices.web.utilities.spec.PosSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PointOfServicesServiceImpl implements PointOfServiceServices {
    private final PointOfServicesRepository posRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CityRepository cityRepository;
    private final AddressRepository addressRepository;

    @Override
    public PointOfServices findById(Long id) throws Exception {
        if (posRepository.findById(id).isEmpty())
            throw new RecordNotFoundException("No pointOfServices found with id = " + id);
        return posRepository.findById(id).get();
    }

    @Override
    public List<PointOfServices> findAll() {
        return posRepository.findAll();
    }

    @Override
    public PointOfServices save(PointOfServices pointOfServices) {
        return posRepository.save(pointOfServices);
    }

    @Override
    public PointOfServices findByName(String name) throws RecordNotFoundException {
        Optional<PointOfServices> pointOfServices = posRepository.findByName(name);
        if (pointOfServices.isEmpty()) throw new RecordNotFoundException("No pointOfService found with name " + name);
        return pointOfServices.get();
    }

    @Override
    public List<PointOfServices> findAllByCity(String city) {
        return posRepository.findAll().stream().filter(t -> t.getAddress().getCity().getName().equals(city)).collect(Collectors.toList());
    }

    @Override
    public List<PointOfServices> findAllByCity(City city) {
        return posRepository.findAllByAddress_City(city);
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

        for (int i = 0; i < userOptional.get().getFavoriteCompanies().size(); i++) {
            if (userOptional.get().getFavoriteCompanies().get(i).getId() != id) {
                continue;
            } else {
                return;
            }
        }
        userOptional.get().getFavoriteCompanies().add(posRepository.findById(id).get());
    }

    @Override
    public List<PointOfServices> findByCategoryLike(String categoryPattern) throws RecordNotFoundException {
        List<PointOfServices> pointsOfServices = posRepository.findByCategoryNameLike(categoryPattern);
        if (pointsOfServices.isEmpty())
            throw new RecordNotFoundException("No records found for category " + categoryPattern);
        return pointsOfServices;
    }

    @Override
    public List<PointOfServices> findByCategoryLikeAndCity(String categoryPattern, City city) throws RecordNotFoundException {
        List<PointOfServices> pointsOfServices = posRepository.findByCategoryNameLikeAndAddress_City(categoryPattern, city);
        if (pointsOfServices.isEmpty())
            throw new RecordNotFoundException("No records found for category " + categoryPattern + " and city " + city.getName());
        return pointsOfServices;
    }

    @Override
    public List<PointOfServices> findByNameLikeAndCategoryNameAndCityName(String posNamePattern, String categoryName, String cityName) throws RecordNotFoundException {
        List<PointOfServices> pointsOfServices = posRepository
                .findAllByNameLikeAndCategoryNameAndAddress_CityName(posNamePattern, categoryName, cityName);
        if (pointsOfServices.isEmpty())
            throw new RecordNotFoundException("No records found for category " + categoryName
                    + " and city " + cityName
                    + " and pointOf Services like" + posNamePattern);
        return pointsOfServices;
    }

    @Override
    public List<PointOfServices> findAllByNameLike(String posNamePattern) {
        return posRepository.findAllByNameContainingIgnoreCase(posNamePattern);
    }

    @Override
    public List<PointOfServices> findAll(Map<String, String> params) {
        final Specification<PointOfServices> specification = params.entrySet().stream()
                .filter(it -> StringUtils.hasText(it.getValue()))
                .map(it -> {
                    if ("city".equals(it.getKey())) {
                        return PosSpec.cityEq(it.getValue());
                    }
                    if ("category".equals(it.getKey())) {
                        return PosSpec.categoryEq(it.getValue());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .reduce(Specification::and)
                .orElse(null);
        return posRepository.findAll(specification);
    }

    @Override
    public List<PointOfServices> findAllByUserBoss(Principal principal) {
        User user = userRepository.findUserByUserName(principal.getName()).orElse(null);
        return posRepository.findAllByBoss(user);
    }

    @Override
    public void deletePosByUser(Principal principal, Long id) {
        Optional<User> optional = userRepository.findUserByUserName(principal.getName());
        if (optional.isEmpty()) {
            return;
        }
        User user = optional.get();
        Optional<PointOfServices> pos = posRepository.findById(id);
        if (pos.isEmpty()) {
            return;
        }
        user.getMasterPos().remove(pos.get());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void saveNewPos(PointOfServiceDto posDto, String category, Principal principal) {
        Category posCategory = categoryRepository.findById(Long.parseLong(category)).orElse(null);
        City city = cityRepository.findByName(posDto.getCity()).orElse(null);
        if (city == null) {
            city = new City();
            city.setCountry("7");
            city.setName(posDto.getCity());
            city.setState(posDto.getState());
            city = cityRepository.save(city);
        }

        User boss = userRepository.findUserByUserName(principal.getName()).orElse(null);

        Address address = addressRepository.findByAddress1(posDto.getAddress()).orElse(null);
        if (address == null) {
            address = new Address();
            address.setZipcode(posDto.getZip());
            address.setAddress1(posDto.getAddress());
            address.setAddress2(null);
            address.setCity(city);
            address = addressRepository.save(address);
        }

        PointOfServices pointToSave = new PointOfServices();
        pointToSave.setName(posDto.getName());
        pointToSave.setAddress(address);
        pointToSave.setBoss(boss);
        pointToSave.setCategory(posCategory);

        posRepository.save(pointToSave);
    }
}

