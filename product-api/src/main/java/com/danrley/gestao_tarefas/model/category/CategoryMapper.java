package com.danrley.gestao_tarefas.model.category;

import com.danrley.gestao_tarefas.dto.category.CategoryRequestDTO;
import com.danrley.gestao_tarefas.dto.category.CategoryResponseDTO;

public class CategoryMapper {

  public static Category toEntity(CategoryRequestDTO dto) {
    return new Category(dto.getName());
  }

  public static CategoryResponseDTO toDTO(Category category) {
    return new CategoryResponseDTO(category.getId(), category.getName());
  }
}
