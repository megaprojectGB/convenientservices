package com.convenientservices.web.services;

import com.convenientservices.web.Exceptions.CityNotFoundException;
import com.convenientservices.web.Exceptions.RecordNotFoundException;
import com.convenientservices.web.entities.City;
import com.convenientservices.web.entities.PointOfServices;

import java.security.Principal;
import java.util.List;

public interface PointOfServiceServices {
    PointOfServices findById (Long id) throws Exception;
    List<PointOfServices> findAll();
    PointOfServices save(PointOfServices pointOfService);
    PointOfServices findByName (String name) throws RecordNotFoundException;
    List<PointOfServices> findAllByCity(String city);
    void deleteFavouriteCompanyByUser(Principal principal, Long id);
    void addFavouriteCompanyByUser(Principal principal, Long id);

    List<PointOfServices> findByCategoryLike(String categoryPattern) throws RecordNotFoundException;
    List<PointOfServices> findByCategoryLikeAndCity(String categoryPattern, City city) throws RecordNotFoundException;
}
