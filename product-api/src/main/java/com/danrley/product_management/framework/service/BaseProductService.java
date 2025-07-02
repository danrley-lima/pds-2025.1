package com.danrley.product_management.framework.service;

import java.util.List;
import java.util.Optional;

import com.danrley.product_management.dto.product.ProductRequestDTO;
import com.danrley.product_management.dto.product.ProductResponseDTO;
import com.danrley.product_management.framework.model.BaseProduct;

/**
 * Interface base para serviços de produtos.
 * Define operações CRUD comuns que todos os domínios devem implementar.
 * 
 * @param <T> Tipo da entidade específica do domínio que implementa BaseProduct
 */
public interface BaseProductService<T extends BaseProduct> {
    
    /**
     * Busca todos os produtos do domínio.
     */
    List<T> getAll();
    
    /**
     * Busca produto por ID.
     */
    Optional<T> getById(Long id);
    
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
