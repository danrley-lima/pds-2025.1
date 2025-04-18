package com.danrley.gestao_tarefas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danrley.gestao_tarefas.dto.category.CategoryRequestDTO;
import com.danrley.gestao_tarefas.dto.category.CategoryResponseDTO;
import com.danrley.gestao_tarefas.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService service) {
    this.categoryService = service;
  }

  @PostMapping
  public CategoryResponseDTO create(@RequestBody CategoryRequestDTO request) {
    return categoryService.create(request);
  }

  @GetMapping
  public List<CategoryResponseDTO> findAll() {
    return categoryService.findAll();
  }

  @GetMapping("/{id}")
  public CategoryResponseDTO findById(@PathVariable Long id) {
    return categoryService.findById(id);
  }

  @PutMapping("/{id}")
  public CategoryResponseDTO update(@PathVariable Long id, @RequestBody CategoryRequestDTO request) {
    return categoryService.update(id, request);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    categoryService.delete(id);
  }
}
