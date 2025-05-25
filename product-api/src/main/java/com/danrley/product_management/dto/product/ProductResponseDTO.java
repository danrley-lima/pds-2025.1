package com.danrley.product_management.dto.product;

import com.danrley.product_management.model.product.UnitType;

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

  public boolean onPromotion;
  public Double promotionalPrice;
}