package com.danrley.product_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danrley.product_management.model.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByName(String name);

}