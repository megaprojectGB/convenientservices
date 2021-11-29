package com.convenientservices.web.services;

import com.convenientservices.web.entities.Category;
import com.convenientservices.web.exceptions.RecordNotFoundException;

import java.util.List;

public interface CategoryService {
    Category findById (Long id) throws RecordNotFoundException;
    List<Category> findAll();
    Category save(Category category);
    Category findByName(String name);
    String findCorrectNameOfCategory (String name);
}
