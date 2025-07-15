package com.danrley.product_management.core.model;

import com.danrley.product_management.core.domain.Domain;

/**
 * Classe abstrata base para entidades de produtos.
 * Define os campos comuns que todos os produtos devem ter.
 */
public abstract class BaseProduct {

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
