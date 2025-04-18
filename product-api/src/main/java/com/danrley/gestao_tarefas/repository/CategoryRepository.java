package com.danrley.gestao_tarefas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danrley.gestao_tarefas.model.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByName(String name);

}