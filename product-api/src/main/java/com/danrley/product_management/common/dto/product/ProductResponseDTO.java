package com.danrley.product_management.common.dto.product;

import java.time.LocalDate;

import com.danrley.product_management.common.model.product.UnitType;
import com.danrley.product_management.domain.construction.model.ConstructionProduct;
import com.danrley.product_management.domain.furniture.model.FurnitureProduct;
import com.danrley.product_management.domain.grocery.model.GroceryProduct;

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
  public boolean priority;
  public boolean onPromotion;
  public Double promotionalPrice;
  public Long categoryId;

  // Campos do domínio grocery
  public LocalDate expirationDate;

  // Campos do domínio furniture
  public String material;
  public String color;

  // Campos do domínio construction
  public String specifications;

  // ========== FACTORY ==========

  public static ProductResponseDTO fromGroceryProduct(GroceryProduct product) {
    ProductResponseDTO dto = new ProductResponseDTO();
    mapBaseFields(product, dto);

    // Campos do grocery
    dto.expirationDate = product.getExpirationDate();

    return dto;
  }

  public static ProductResponseDTO fromFurnitureProduct(FurnitureProduct product) {
    ProductResponseDTO dto = new ProductResponseDTO();
    mapBaseFields(product, dto);

    // Campos do furniture
    dto.material = product.getMaterial();
    dto.color = product.getColor();

    return dto;
  }

  public static ProductResponseDTO fromConstructionProduct(ConstructionProduct product) {
    ProductResponseDTO dto = new ProductResponseDTO();
    mapBaseFields(product, dto);

    // Campos do construction
    dto.specifications = product.getSpecifications();

    return dto;
  }

  private static void mapBaseFields(Object product, ProductResponseDTO dto) {
    // Mapeamento de campos base usando cast para BaseProduct
    if (product instanceof GroceryProduct) {
      GroceryProduct p = (GroceryProduct) product;
      dto.id = p.getId();
      dto.name = p.getName();
      dto.brand = p.getBrand();
      dto.unitWeight = p.getUnitWeight();
      dto.unitType = p.getUnitType();
      dto.stockQuantity = p.getStockQuantity();
      dto.unitPrice = p.getUnitPrice();
      dto.available = p.isAvailable();
      dto.priority = p.isPriority();
      dto.categoryName = p.getCategory() != null ? p.getCategory().getName() : null;
      dto.categoryId = p.getCategory() != null ? p.getCategory().getId() : null;
    } else if (product instanceof FurnitureProduct) {
      FurnitureProduct p = (FurnitureProduct) product;
      dto.id = p.getId();
      dto.name = p.getName();
      dto.brand = p.getBrand();
      dto.unitWeight = p.getUnitWeight();
      dto.unitType = p.getUnitType();
      dto.stockQuantity = p.getStockQuantity();
      dto.unitPrice = p.getUnitPrice();
      dto.available = p.isAvailable();
      dto.priority = p.isPriority();
      dto.categoryName = p.getCategory() != null ? p.getCategory().getName() : null;
      dto.categoryId = p.getCategory() != null ? p.getCategory().getId() : null;
    } else if (product instanceof ConstructionProduct) {
      ConstructionProduct p = (ConstructionProduct) product;
      dto.id = p.getId();
      dto.name = p.getName();
      dto.brand = p.getBrand();
      dto.unitWeight = p.getUnitWeight();
      dto.unitType = p.getUnitType();
      dto.stockQuantity = p.getStockQuantity();
      dto.unitPrice = p.getUnitPrice();
      dto.available = p.isAvailable();
      dto.priority = p.isPriority();
      dto.categoryName = p.getCategory() != null ? p.getCategory().getName() : null;
      dto.categoryId = p.getCategory() != null ? p.getCategory().getId() : null;
    }
  }
}