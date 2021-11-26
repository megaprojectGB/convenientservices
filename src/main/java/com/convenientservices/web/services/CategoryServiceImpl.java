package com.convenientservices.web.services;

import com.convenientservices.web.Exceptions.RecordNotFoundException;
import com.convenientservices.web.entities.Category;
import com.convenientservices.web.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository repository;

    @Override
    public Category findById (Long id) throws RecordNotFoundException {
        return repository.findById(id).orElseThrow(() -> new RecordNotFoundException("Category with id = " + id + "not found"));
    }

    @Override
    public List<Category> findAll () {
        return repository.findAll();
    }

    @Override
    public Category save (Category category) {
        return repository.save(category);
    }

    @Override
    public Category findByName (String name) {
        return repository.findByName(name).orElse(null);
    }
}
