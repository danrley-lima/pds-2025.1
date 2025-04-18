package com.danrley.gestao_tarefas.dto.category;

public class CategoryResponseDTO {
  private Long id;
  private String name;

  public CategoryResponseDTO() {
  }

  public CategoryResponseDTO(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
