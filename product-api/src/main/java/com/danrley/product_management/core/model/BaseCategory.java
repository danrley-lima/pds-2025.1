package com.danrley.product_management.core.model;

import com.danrley.product_management.core.domain.Domain;

/**
 * Interface base para todas as categorias no framework.
 * Define os campos comuns que todas as categorias devem ter, independente do domínio.
 */
public interface BaseCategory {

    /**
     * Identificador único da categoria.
     */
    Long getId();

    /**
     * Nome da categoria.
     */
    String getName();

    /**
     * Domínio ao qual esta categoria pertence.
     */
    Domain getDomain();

    /**
     * Categoria pai (para hierarquia de categorias).
     * Retorna null se for uma categoria raiz.
     */
    BaseCategory getParentCategory();

    /**
     * Descrição opcional da categoria.
     */
    String getDescription();
}
