package com.danrley.gestao_tarefas.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.danrley.gestao_tarefas.dto.category.CategoryRequestDTO;
import com.danrley.gestao_tarefas.dto.category.CategoryResponseDTO;
import com.danrley.gestao_tarefas.model.category.Category;
import com.danrley.gestao_tarefas.model.category.CategoryMapper;
import com.danrley.gestao_tarefas.repository.CategoryRepository;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository repository) {
    this.categoryRepository = repository;
  }

  public CategoryResponseDTO create(CategoryRequestDTO request) {
    Category category = CategoryMapper.toEntity(request);
    return CategoryMapper.toDTO(categoryRepository.save(category));
  }

  public List<CategoryResponseDTO> findAll() {
    return categoryRepository.findAll()
        .stream()
        .map(CategoryMapper::toDTO)
        .collect(Collectors.toList());
  }

  public CategoryResponseDTO findById(Long id) {
    return categoryRepository.findById(id)
        .map(CategoryMapper::toDTO)
        .orElseThrow(() -> new RuntimeException("Category not found"));
  }

  public CategoryResponseDTO update(Long id, CategoryRequestDTO request) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Category not found"));

    category.setName(request.getName());
    return CategoryMapper.toDTO(categoryRepository.save(category));
  }

  public void delete(Long id) {
    categoryRepository.deleteById(id);
  }
}
