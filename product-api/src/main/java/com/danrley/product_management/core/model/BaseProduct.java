package com.danrley.product_management.core.model;

import com.danrley.product_management.core.domain.Domain;

/**
 * Interface base para entidades de produtos.
 * Define os campos comuns que todos os produtos devem ter.
 */
public interface BaseProduct {

  /**
   * Identificador único do produto.
   */
  Long getId();

  /**
   * Nome do produto.
   */
  String getName();

  /**
   * Marca/fabricante do produto.
   */
  String getBrand();

  /**
   * Preço unitário do produto.
   */
  Double getUnitPrice();

  /**
   * Categoria do produto.
   */
  BaseCategory getCategory();

  /**
   * Indica se o produto está disponível.
   */
  boolean isAvailable();

  /**
   * Indica se o produto tem prioridade.
   */
  boolean isPriority();

  /**
   * Quantidade em estoque.
   */
  Integer getStockQuantity();

  /**
   * Domínio ao qual este produto pertence.
   */
  Domain getDomain();

  /**
   * Serializa o produto para prompt de IA.
   */
  String serializeForPrompt();
}
