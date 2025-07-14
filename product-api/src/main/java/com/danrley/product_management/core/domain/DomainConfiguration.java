package com.danrley.product_management.core.domain;

import com.danrley.product_management.core.model.BaseProduct;
import com.danrley.product_management.core.model.BaseCategory;

/**
 * Interface que define a configuração de um domínio.
 * Cada domínio implementa esta interface para definir suas regras e comportamentos.
 */
public interface DomainConfiguration {

    /**
     * Retorna o domínio associado a esta configuração.
     */
    Domain getDomain();

    /**
     * Retorna as categorias padrão para este domínio.
     */
    BaseCategory[] getDefaultCategories();

    /**
     * Retorna o prompt template para busca de produtos neste domínio.
     */
    String getProductSearchPromptTemplate();

    /**
     * Retorna o prompt template para recomendações de receitas/projetos neste domínio.
     */
    String getRecipePromptTemplate();

    /**
     * Retorna o prompt template para promoções neste domínio.
     */
    String getPromotionPromptTemplate();

    /**
     * Retorna as unidades de medida suportadas por este domínio.
     */
    String[] getSupportedUnits();

    /**
     * Retorna o formato JSON esperado para respostas de produtos.
     */
    String getProductResponseJsonFormat();

    /**
     * Retorna o formato JSON esperado para respostas de receitas/projetos.
     */
    String getRecipeResponseJsonFormat();

    /**
     * Valida se um produto é válido para este domínio.
     */
    boolean isValidProduct(BaseProduct product);

    /**
     * Formata uma mensagem de produto para o prompt.
     */
    String formatProductForPrompt(BaseProduct product);
}
