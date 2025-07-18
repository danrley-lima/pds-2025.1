package com.danrley.product_management.domain.construction.model;

import com.danrley.product_management.common.model.category.Category;
import com.danrley.product_management.common.model.product.UnitType;
import com.danrley.product_management.core.domain.Domain;
import com.danrley.product_management.core.model.BaseCategory;
import com.danrley.product_management.core.model.BaseProduct;
import com.danrley.product_management.core.model.BasePromotion;
import com.danrley.product_management.domain.grocery.model.GroceryPromotion;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Produto específico para o domínio de construção.
 * Entidade JPA para produtos do domínio construction.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "construction_products")
 public class ConstructionProduct extends BaseProduct {

  @Column(name = "unit_weight")
  private Double unitWeight;

  @Enumerated(EnumType.STRING)
  @Column(name = "unit_type")
  private UnitType unitType;

  @Column(name = "unit_price")
  private Double unitPrice;

  private boolean priority = false;

  @Nullable
  @OneToOne(mappedBy = "constructionProduct", cascade = CascadeType.ALL, orphanRemoval = true)
  protected ConstructionPromotion constructionPromotion;

  private String specifications;


  public ConstructionProduct() {
    super();
  }

  public ConstructionProduct(String name, String brand, Double unitWeight, UnitType unitType,
      Integer stockQuantity, Double unitPrice, Category category,
      boolean available, boolean priority, String specifications) {
    super(name, brand, stockQuantity, category, available);
    this.unitWeight = unitWeight;
    this.unitType = unitType;
    this.unitPrice = unitPrice;
    this.priority = priority;
    this.specifications = specifications;
  }

  @Override
  public Domain getDomain() {
    return Domain.CONSTRUCTION;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getBrand() {
    return brand;
  }

  @Override
  public Double getUnitPrice() {
    return unitPrice;
  }

  @Override
  public BaseCategory getCategory() {
    return category;
  }

  @Override
  public Integer getStockQuantity() {
    return stockQuantity;
  }


  @Override
  public boolean isAvailable() {
    return available;
  }

  @Override
  public boolean isPriority() {
    return priority;
  }

  public ConstructionPromotion getConstructionPromotion() {
    return constructionPromotion;
  }

  public void setConstructionPromotion(ConstructionPromotion constructionPromotion) {
    this.constructionPromotion = constructionPromotion;
  }

  @Override
  public String serializeForPrompt() {
    return String.format("%d,%s,%s,%s,%.2f,%b,%b,%d,%b,especificacoes=%s",
        id, name, brand != null ? brand : "",
        category != null ? category.getName() : "",
        unitPrice, available, false, // promotion placeholder
        stockQuantity, priority,
        specifications != null ? specifications : "");
  }
}
