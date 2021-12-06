package com.convenientservices.web.services;

import com.convenientservices.web.dto.PointOfServiceDto;
import com.convenientservices.web.entities.Category;
import com.convenientservices.web.entities.User;
import com.convenientservices.web.exceptions.RecordNotFoundException;
import com.convenientservices.web.entities.City;
import com.convenientservices.web.entities.PointOfServices;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface PointOfServiceServices {
    PointOfServices findById (Long id) throws Exception;
    List<PointOfServices> findAll();
    List<PointOfServices> findAll(Map<String, String> param);
    PointOfServices save(PointOfServices pointOfService);
    PointOfServices findByName (String name) throws RecordNotFoundException;
    List<PointOfServices> findAllByCity(String city);
    List<PointOfServices> findAllByCity(City city);
    void deleteFavouriteCompanyByUser(Principal principal, Long id);
    void addFavouriteCompanyByUser(Principal principal, Long id);
    List<PointOfServices> findAllByUserBoss(Principal principal);
    List<PointOfServices> findByCategoryLike(String categoryPattern) throws RecordNotFoundException;
    List<PointOfServices> findByCategoryLikeAndCity(String categoryPattern, City city) throws RecordNotFoundException;
    List<PointOfServices> findByNameLikeAndCategoryNameAndCityName(String posNamePattern,
                                                                   String categoryName,
                                                                   String cityName) throws RecordNotFoundException;
    List<PointOfServices> findAllByNameLike(String posNamePattern, String city, String category);
    void deletePosByUser(Principal principal, Long id);

    void saveNewPos(PointOfServiceDto posDto, String category, Principal principal);

    void editNewPos(PointOfServiceDto posDto, String category, Principal principal);

    PointOfServiceDto getPointForEdit(Long id);

    List<User> getMastersForPos(Long id);

    void deleteMasterFromPos(Long id, Long posId);
}
