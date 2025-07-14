package com.danrley.product_management.domain.grocery.model;

import java.time.LocalDate;

import com.danrley.product_management.common.model.category.Category;
import com.danrley.product_management.common.model.product.UnitType;
import com.danrley.product_management.common.model.promotion.Promotion;
import com.danrley.product_management.core.domain.Domain;
import com.danrley.product_management.core.model.BaseCategory;
import com.danrley.product_management.core.model.BaseProduct;

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
import lombok.NoArgsConstructor;

/**
 * Produto específico para o domínio de supermercado.
 * Entidade JPA para produtos do domínio grocery.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grocery_products")
public class GroceryProduct implements BaseProduct {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String brand;
  private Double unitWeight;

  @Enumerated(EnumType.STRING)
  private UnitType unitType;

  private Integer stockQuantity;
  private Double unitPrice;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  private boolean available;
  private boolean priority;

  private LocalDate expirationDate;

  @OneToOne(mappedBy = "groceryProduct", cascade = CascadeType.ALL, orphanRemoval = true)
  private Promotion promotion;

  // Construtor específico
  public GroceryProduct(String name, String brand, Double unitWeight,
      UnitType unitType, Integer stockQuantity, Double unitPrice,
      Category category, boolean available, boolean priority) {
    this.name = name;
    this.brand = brand;
    this.unitWeight = unitWeight;
    this.unitType = unitType;
    this.stockQuantity = stockQuantity;
    this.unitPrice = unitPrice;
    this.category = category;
    this.available = available;
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
        promotion != null,
        promotion != null ? promotion.getPromotionalPrice() : unitPrice,
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

  public Promotion getPromotion() {
    return promotion;
  }

  public void setPromotion(Promotion promotion) {
    this.promotion = promotion;
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
