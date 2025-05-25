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
}