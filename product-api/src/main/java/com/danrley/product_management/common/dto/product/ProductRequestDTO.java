package com.danrley.product_management.common.dto.product;

import java.time.LocalDate;

import com.danrley.product_management.common.model.product.UnitType;

public class ProductRequestDTO {
  public String name;
  public String brand;
  public Double unitWeight;
  public UnitType unitType;
  public Integer stockQuantity;
  public Double unitPrice;
  public Long categoryId;
  public Boolean priority;
  public Boolean available;

  public LocalDate expirationDate;
  public String material;
  public String color;
  public String specifications;
}