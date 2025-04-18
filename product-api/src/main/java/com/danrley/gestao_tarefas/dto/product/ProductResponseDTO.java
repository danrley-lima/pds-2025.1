package com.danrley.gestao_tarefas.dto.product;

import com.danrley.gestao_tarefas.model.product.UnitType;

public class ProductResponseDTO {
  public Long id;
  public String name;
  public String brand;
  public Double unitWeight;
  public UnitType unitType;
  public Integer stockQuantity;
  public Double unitPrice;
  public boolean available;
  public String categoryName;
}