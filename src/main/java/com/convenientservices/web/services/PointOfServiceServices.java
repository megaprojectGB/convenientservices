package com.convenientservices.web.services;

import com.convenientservices.web.entities.PointOfServices;

import java.util.List;

public interface PointOfServiceServices {
    PointOfServices findById (Long id) throws Exception;
    List<PointOfServices> findAll();
    PointOfServices save(PointOfServices pointOfService);
    PointOfServices findByName (String name);
}