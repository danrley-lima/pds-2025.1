package com.danrley.product_management.core.model;

import com.danrley.product_management.core.domain.Domain;

/**
 * Classe abstrata base para todas as categorias no framework.
 * Define os campos comuns que todas as categorias devem ter, independente do
 * domínio.
 */
public abstract class BaseCategory {

  /**
   * Identificador único da categoria.
   */
  public abstract Long getId();

  /**
   * Nome da categoria.
   */
  public abstract String getName();

  /**
   * Domínio ao qual esta categoria pertence.
   */
  public abstract Domain getDomain();

  /**
   * Categoria pai (para hierarquia de categorias).
   * Retorna null se for uma categoria raiz.
   */
  public abstract BaseCategory getParentCategory();

  /**
   * Descrição opcional da categoria.
   */
  public abstract String getDescription();
}
