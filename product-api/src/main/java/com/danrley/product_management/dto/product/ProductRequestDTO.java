package com.danrley.product_management.dto.product;

import com.danrley.product_management.model.product.UnitType;

public class ProductRequestDTO {
  public String name;
  public String brand;
  public Double unitWeight;
  public UnitType unitType;
  public Integer stockQuantity;
  public Double unitPrice;
  public Long categoryId;
  public Boolean priority; // Alterado de boolean para Boolean para permitir null
  public Boolean available; // Para controlar disponibilidade
  
  // Campos específicos do domínio furniture
  public String dimensions;
  public String material;
  public String color;
  public String style;

  // Campos específicos do domínio construction
  public String specifications;
  public String constructionCategory;
  public String application;
  public String grade;
}