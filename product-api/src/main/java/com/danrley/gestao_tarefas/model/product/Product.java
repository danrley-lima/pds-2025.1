package com.danrley.gestao_tarefas.model.product;

import com.danrley.gestao_tarefas.model.category.Category;
import com.danrley.gestao_tarefas.model.promotion.Promotion;

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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

  public Product(String name, String brand, Double unitWeight, UnitType unitType, Integer stockQuantity,
      Double unitPrice, Category category, boolean available) {
    this.name = name;
    this.brand = brand;
    this.unitWeight = unitWeight;
    this.unitType = unitType;
    this.stockQuantity = stockQuantity;
    this.unitPrice = unitPrice;
    this.category = category;
    this.available = available;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String brand;

  private Double unitWeight; // peso por unidade

  @Enumerated(EnumType.STRING)
  private UnitType unitType; // tipo da unidade: G, KG, ML, L, UN

  private Integer stockQuantity; // quantas unidades temos no estoque

  private Double unitPrice;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  private boolean available;

  @OneToOne(mappedBy = "product")
  private Promotion promotion;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
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

  public Integer getStockQuantity() {
    return stockQuantity;
  }

  public void setStockQuantity(Integer stockQuantity) {
    this.stockQuantity = stockQuantity;
  }

  public Double getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(Double unitPrice) {
    this.unitPrice = unitPrice;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }

}
