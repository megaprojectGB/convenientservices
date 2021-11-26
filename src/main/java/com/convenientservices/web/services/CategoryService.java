package com.convenientservices.web.services;

import com.convenientservices.web.Exceptions.RecordNotFoundException;
import com.convenientservices.web.entities.Category;

import java.util.List;

public interface CategoryService {
    Category findById (Long id) throws Exception;
    List<Category> findAll();
    Category save(Category category);
    Category findByName(String name);
}
