package com.danrley.product_management.core.service;

import java.util.List;
import java.util.Optional;

import com.danrley.product_management.common.dto.product.ProductRequestDTO;
import com.danrley.product_management.common.dto.product.ProductResponseDTO;
import com.danrley.product_management.core.model.BaseProduct;
import com.danrley.product_management.domain.grocery.model.GroceryProduct;

/**
 * Interface base para serviços de produtos.
 * @param <T> Tipo da entidade que implementa BaseProduct
 */
public interface BaseProductService<T extends BaseProduct> {
    
    /**
     * Busca todos os produtos.
     */
    List<T> getAll();
    
    /**
     * Busca produto por ID.
     */
    Optional<T> getById(Long id);

    List<T> getProductsByIds(List<Long> ids);

    /**
     * Cria um novo produto.
     */
    T create(ProductRequestDTO dto);
    
    /**
     * Atualiza um produto existente.
     */
    T update(Long id, ProductRequestDTO dto);
    
    /**
     * Remove um produto.
     */
    void delete(Long id);
    
    /**
     * Converte entidade para DTO de resposta.
     */
    ProductResponseDTO toResponseDTO(T entity);
    
    /**
     * Busca produtos por categoria.
     */
    List<T> getByCategory(String category);
    
    /**
     * Busca produtos por marca.
     */
    List<T> getByBrand(String brand);
    
    /**
     * Busca produtos disponíveis (em estoque).
     */
    List<T> getAvailable();
}
