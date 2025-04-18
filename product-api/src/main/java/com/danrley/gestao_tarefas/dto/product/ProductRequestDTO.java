package com.danrley.gestao_tarefas.dto.product;

import com.danrley.gestao_tarefas.model.product.UnitType;

public class ProductRequestDTO {
  public String name;
  public String brand;
  public Double unitWeight;
  public UnitType unitType;
  public Integer stockQuantity;
  public Double unitPrice;
  public Long categoryId;
}