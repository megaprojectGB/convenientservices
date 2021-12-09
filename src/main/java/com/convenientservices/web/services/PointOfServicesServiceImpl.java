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
    public List<PointOfServices> findAllByNameLike(String posNamePattern, String city, String category) {
        List<PointOfServices> pointsOfServices = posRepository.findAllByNameContainingIgnoreCase(posNamePattern);
        if (city != null) {
            pointsOfServices = pointsOfServices.stream()
                    .filter((pos) -> pos.getAddress().getCity().getName().equals(city))
                    .collect(Collectors.toList());
        }
        if (category != null) {
            pointsOfServices = pointsOfServices.stream()
                    .filter((pos) -> pos.getCategory().getName().equals(category))
                    .collect(Collectors.toList());
        }
        return pointsOfServices;
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

    @Override
    public PointOfServiceDto getPointForEdit(Long id) {
        Optional<PointOfServices> pos = posRepository.findById(id);
        if (pos.isEmpty()) {
            return null;
        }
        PointOfServiceDto dto = new PointOfServiceDto();
        dto.setId(pos.get().getId());
        dto.setName(pos.get().getName());
        dto.setCategory(pos.get().getCategory().getName());
        dto.setZip(pos.get().getAddress().getZipcode());
        dto.setState(pos.get().getAddress().getCity().getState());
        dto.setCity(pos.get().getAddress().getCity().getName());
        dto.setAddress(pos.get().getAddress().getAddress1());
        dto.setSelector(pos.get().getCategory().getId());
        return dto;
    }

    @Override
    @Transactional
    public void editNewPos(PointOfServiceDto posDto, String category, Principal principal) {
        Optional<PointOfServices> pos = posRepository.findById(posDto.getId());
        if (pos.isEmpty()) {
            return;
        }
        if (!posDto.getName().equals(pos.get().getName())) {
            pos.get().setName(posDto.getName());
        }

        if (!posDto.getCategory().equals(pos.get().getCategory().getId().toString())) {
            Category cat = categoryRepository.findById(Long.parseLong(posDto.getCategory())).orElse(null);
            pos.get().setCategory(cat);
        }

        Address address = pos.get().getAddress();
        if (!address.getZipcode().equals(posDto.getZip())) {
            address.setZipcode(posDto.getZip());
        }

        if (!address.getAddress1().equals(posDto.getAddress())) {
            address.setAddress1(posDto.getAddress());
        }

        City city = address.getCity();
        if (!city.getName().equals(posDto.getCity())) {
            City tmpCity = cityRepository.findByName(posDto.getName()).orElse(null);
            if (tmpCity != null && tmpCity.getState().equals(posDto.getState())) {
                city = tmpCity;
            }
            if (tmpCity == null) {
                City c = new City();
                c.setCountry("7");
                c.setName(posDto.getCity());
                c.setState(posDto.getState());
                city = cityRepository.save(c);
            }
        }
        if (city.getName().equals(posDto.getCity()) && !city.getState().equals(posDto.getState())) {
            City tmpCity = cityRepository.findByNameAndState(posDto.getCity(), posDto.getState()).orElse(null);
            if (tmpCity == null) {
                City c = new City();
                c.setCountry("7");
                c.setName(posDto.getCity());
                c.setState(posDto.getState());
                city = cityRepository.save(c);
            } else {
                city = tmpCity;
            }
        }

        address.setCity(city);
        pos.get().setAddress(address);
    }

    @Override
    public List<User> getMastersForPos(Long id) {
        Optional<PointOfServices> posOpt = posRepository.findById(id);
//        List<Passenger> findByOrderBySeatNumberAsc();
        if (posOpt.isEmpty()) {
            return null;
        }
        return posOpt.get().getUsers();
    }

    @Override
    @Transactional
    public void deleteMasterFromPos(Long id, Long posId) {
        PointOfServices posOpt = posRepository.findById(posId).get();
        User master = userRepository.findById(id).get();
        master.getMasterPos().remove(posOpt);
    }
}

