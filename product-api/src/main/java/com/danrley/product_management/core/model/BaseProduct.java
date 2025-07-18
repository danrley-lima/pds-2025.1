package com.danrley.product_management.core.model;

import com.danrley.product_management.common.model.category.Category;
import com.danrley.product_management.core.domain.Domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import lombok.Data;

/**
 * Classe abstrata base para entidades de produtos.
 * Define os campos comuns que todos os produtos devem ter.
 */
@MappedSuperclass
@Data
public abstract class BaseProduct {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  protected String name;
  protected String brand;
  protected Integer stockQuantity;

  @ManyToOne
  @JoinColumn(name = "category_id")
  protected Category category;

  protected boolean available;

  public BaseProduct() {
  }

  public BaseProduct(String name, String brand, Integer stockQuantity, Category category, boolean available) {
    this.name = name;
    this.brand = brand;
    this.stockQuantity = stockQuantity;
    this.category = category;
    this.available = available;
  }

  /**
   * Identificador único do produto.
   */
  public abstract Long getId();

  /**
   * Nome do produto.
   */
  public abstract String getName();

  /**
   * Marca/fabricante do produto.
   */
  public abstract String getBrand();

  /**
   * Preço unitário do produto.
   */
  public abstract Double getUnitPrice();

  /**
   * Categoria do produto.
   */
  public abstract BaseCategory getCategory();

  /**
   * Indica se o produto está disponível.
   */
  public abstract boolean isAvailable();

  /**
   * Indica se o produto tem prioridade.
   */
  public abstract boolean isPriority();

  /**
   * Quantidade em estoque.
   */
  public abstract Integer getStockQuantity();

  /**
   * Domínio ao qual este produto pertence.
   */
  public abstract Domain getDomain();

  /**
   * Serializa o produto para prompt de IA.
   */
  public abstract String serializeForPrompt();
}
