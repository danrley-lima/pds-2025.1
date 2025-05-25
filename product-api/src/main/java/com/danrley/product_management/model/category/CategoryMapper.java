package com.danrley.product_management.model.category;

import com.danrley.product_management.dto.category.CategoryRequestDTO;
import com.danrley.product_management.dto.category.CategoryResponseDTO;

public class CategoryMapper {

  public static Category toEntity(CategoryRequestDTO dto) {
    return new Category(dto.getName());
  }

  public static CategoryResponseDTO toDTO(Category category) {
    return new CategoryResponseDTO(category.getId(), category.getName());
  }
}
