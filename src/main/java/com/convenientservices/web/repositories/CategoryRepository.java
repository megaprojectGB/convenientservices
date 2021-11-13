package com.convenientservices.web.repositories;

import com.convenientservices.web.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
