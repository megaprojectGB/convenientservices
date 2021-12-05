package com.convenientservices.web.services;

import com.convenientservices.web.exceptions.RecordNotFoundException;
import com.convenientservices.web.entities.Category;
import com.convenientservices.web.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

    @Override
    public String findCorrectNameOfCategory (String name) {
        if (name != null) {
            List<Category> categories = repository.findAll().stream()
                    .filter(category -> category.getName().toLowerCase(Locale.ROOT).equals(name.toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
            if (!categories.isEmpty()) {
                return categories.get(0).getName();
            }
        }
        return null;
    }
}
