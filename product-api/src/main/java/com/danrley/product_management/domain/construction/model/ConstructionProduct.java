package com.danrley.product_management.domain.construction.model;

import com.danrley.product_management.common.model.category.Category;
import com.danrley.product_management.common.model.product.UnitType;
import com.danrley.product_management.core.domain.Domain;
import com.danrley.product_management.core.model.BaseProduct;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Produto específico para o domínio de construção.
 * Entidade JPA para produtos do domínio construction.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "construction_products")
public class ConstructionProduct implements BaseProduct {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  private String brand;

  @Column(name = "unit_weight")
  private Double unitWeight;

  @Enumerated(EnumType.STRING)
  @Column(name = "unit_type")
  private UnitType unitType;

  @Column(name = "stock_quantity")
  private Integer stockQuantity;

  @Column(name = "unit_price")
  private Double unitPrice;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  private boolean available = true;

  private boolean priority = false;

  private String specifications;

  public ConstructionProduct(String name, String brand, Double unitWeight, UnitType unitType,
      Integer stockQuantity, Double unitPrice, Category category,
      boolean available, boolean priority, String specifications) {
    this.name = name;
    this.brand = brand;
    this.unitWeight = unitWeight;
    this.unitType = unitType;
    this.stockQuantity = stockQuantity;
    this.unitPrice = unitPrice;
    this.category = category;
    this.available = available;
    this.priority = priority;
    this.specifications = specifications;
  }

  @Override
  public Domain getDomain() {
    return Domain.CONSTRUCTION;
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
  public String serializeForPrompt() {
    return String.format("%d,%s,%s,%s,%.2f,%b,%b,%d,%b,especificacoes=%s",
        id, name, brand != null ? brand : "",
        category != null ? category.getName() : "",
        unitPrice, available, false, // promotion placeholder
        stockQuantity, priority,
        specifications != null ? specifications : "");
  }
}
