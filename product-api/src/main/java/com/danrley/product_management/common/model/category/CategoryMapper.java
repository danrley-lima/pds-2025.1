package com.danrley.product_management.common.model.category;

import com.danrley.product_management.common.dto.category.CategoryRequestDTO;
import com.danrley.product_management.common.dto.category.CategoryResponseDTO;

public class CategoryMapper {

  public static Category toEntity(CategoryRequestDTO dto) {
    return new Category(dto.getName());
  }

  public static CategoryResponseDTO toDTO(Category category) {
    return new CategoryResponseDTO(category.getId(), category.getName());
  }
}
