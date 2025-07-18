package com.danrley.product_management.domain.grocery.model;

import java.time.LocalDate;

import com.danrley.product_management.common.model.category.Category;
import com.danrley.product_management.common.model.product.UnitType;
import com.danrley.product_management.core.domain.Domain;
import com.danrley.product_management.core.model.BaseCategory;
import com.danrley.product_management.core.model.BaseProduct;
import com.danrley.product_management.core.model.BasePromotion;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Produto para o domínio de supermercado.
 * Entidade JPA para produtos do domínio grocery.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "grocery_products")
 public class GroceryProduct extends BaseProduct {
  private Double unitWeight;
  private Double unitPrice;

  @Enumerated(EnumType.STRING)
  private UnitType unitType;

  private boolean priority;

  private LocalDate expirationDate;

  @Nullable
  @OneToOne(mappedBy = "groceryProduct", cascade = CascadeType.ALL, orphanRemoval = true)
  protected GroceryPromotion groceryPromotion;

  public GroceryProduct() {
    super();
  }

  public GroceryProduct(String name, String brand, Double unitWeight,
      UnitType unitType, Integer stockQuantity, Double unitPrice,
      Category category, boolean available, boolean priority) {
    super(name, brand, stockQuantity, category, available);
    this.unitWeight = unitWeight;
    this.unitType = unitType;
    this.unitPrice = unitPrice;
    this.expirationDate = expirationDate;
    this.priority = priority;
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
  public boolean isAvailable() {
    return available;
  }

  @Override
  public boolean isPriority() {
    return priority;
  }

  @Override
  public Integer getStockQuantity() {
    return stockQuantity;
  }

  @Override
  public Domain getDomain() {
    return Domain.GROCERY;
  }

  @Override
  public String serializeForPrompt() {
    return String.format("%d,%s,%s,%s,%.1f %s,%.2f,%b,%b,%.2f,%d,%b",
        id,
        name,
        brand != null ? brand : "",
        category != null ? category.getName() : "",
        unitWeight != null ? unitWeight : 0.0,
        unitType != null ? unitType.toString() : "UN",
        unitPrice,
        available,
        groceryPromotion != null,
        groceryPromotion != null ? groceryPromotion.getPromotionalPrice() : unitPrice,
        stockQuantity,
        priority);
  }

  public Double getUnitWeight() {
    return unitWeight;
  }

  public void setUnitWeight(Double unitWeight) {
    this.unitWeight = unitWeight;
  }

  public UnitType getUnitType() {
    return unitType;
  }

  public void setUnitType(UnitType unitType) {
    this.unitType = unitType;
  }

  public Category getGroceryCategory() {
    return category;
  }

  public void setGroceryCategory(Category category) {
    this.category = category;
  }

  public GroceryPromotion getGroceryPromotion() {
    return groceryPromotion;
  }

  public void setGroceryPromotion(GroceryPromotion groceryPromotion) {
    this.groceryPromotion = groceryPromotion;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setUnitPrice(Double unitPrice) {
    this.unitPrice = unitPrice;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }

  public void setPriority(boolean priority) {
    this.priority = priority;
  }

  public void setStockQuantity(Integer stockQuantity) {
    this.stockQuantity = stockQuantity;
  }

  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(LocalDate expirationDate) {
    this.expirationDate = expirationDate;
  }

}
