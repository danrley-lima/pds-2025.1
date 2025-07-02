package com.danrley.product_management.dto.product;

import java.time.LocalDate;

import com.danrley.product_management.domain.construction.model.ConstructionProduct;
import com.danrley.product_management.domain.furniture.model.FurnitureProduct;
import com.danrley.product_management.domain.grocery.model.GroceryProduct;
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
  public boolean priority;
  public boolean onPromotion;
  public Double promotionalPrice;
  public Long categoryId;
  
  // Campos específicos do domínio grocery
  public LocalDate expirationDate;
  public String nutritionalInfo;
  public Boolean organic;
  
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
  
  // ========== FACTORY METHODS ==========
  
  public static ProductResponseDTO fromGroceryProduct(GroceryProduct product) {
    ProductResponseDTO dto = new ProductResponseDTO();
    mapBaseFields(product, dto);
    
    // Campos específicos do grocery
    dto.expirationDate = product.getExpirationDate();
    dto.nutritionalInfo = product.getNutritionalInfo();
    dto.organic = product.getOrganic();
    
    return dto;
  }
  
  public static ProductResponseDTO fromFurnitureProduct(FurnitureProduct product) {
    ProductResponseDTO dto = new ProductResponseDTO();
    mapBaseFields(product, dto);
    
    // Campos específicos do furniture
    dto.dimensions = product.getDimensions();
    dto.material = product.getMaterial();
    dto.color = product.getColor();
    dto.style = product.getStyle();
    
    return dto;
  }
  
  public static ProductResponseDTO fromConstructionProduct(ConstructionProduct product) {
    ProductResponseDTO dto = new ProductResponseDTO();
    mapBaseFields(product, dto);
    
    // Campos específicos do construction
    dto.specifications = product.getSpecifications();
    dto.constructionCategory = product.getConstructionCategory();
    dto.application = product.getApplication();
    dto.grade = product.getGrade();
    
    return dto;
  }
  
  private static void mapBaseFields(Object product, ProductResponseDTO dto) {
    // Este método será implementado usando reflection ou cast para BaseProduct
    // Por enquanto, vamos usar cast genérico
    if (product instanceof GroceryProduct) {
      GroceryProduct p = (GroceryProduct) product;
      dto.id = p.getId();
      dto.name = p.getName();
      dto.brand = p.getBrand();
      dto.unitWeight = p.getUnitWeight();
      dto.unitType = p.getUnitType();
      dto.stockQuantity = p.getStockQuantity();
      dto.unitPrice = p.getUnitPrice();
      dto.available = p.getAvailable();
      dto.priority = p.getPriority();
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
      dto.available = p.getAvailable();
      dto.priority = p.getPriority();
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
      dto.available = p.getAvailable();
      dto.priority = p.getPriority();
      dto.categoryName = p.getCategory() != null ? p.getCategory().getName() : null;
      dto.categoryId = p.getCategory() != null ? p.getCategory().getId() : null;
    }
  }
}