package com.danrley.product_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danrley.product_management.framework.domain.Domain;
import com.danrley.product_management.model.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByName(String name);
  
  // Métodos para filtrar por domínio
  List<Category> findByDomain(Domain domain);
  
  Optional<Category> findByNameAndDomain(String name, Domain domain);
  
  long countByDomain(Domain domain);
}