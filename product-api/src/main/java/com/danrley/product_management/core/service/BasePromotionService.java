package com.danrley.product_management.core.service;

import java.util.List;
import java.util.Optional;

import com.danrley.product_management.common.dto.promotion.PromotionRequestDTO;
import com.danrley.product_management.common.dto.promotion.PromotionResponseDTO;
import com.danrley.product_management.core.model.BasePromotion;

public interface BasePromotionService<T extends BasePromotion> {
        /**
     * Busca todos os produtos.
     */
    List<T> getAll();
    
    /**
     * Busca produto por ID.
     */
    Optional<T> getById(Long id);
    
    /**
     * Cria um novo produto.
     */
    T create(PromotionRequestDTO dto);
    
    /**
     * Atualiza um produto existente.
     */
    T update(Long id, PromotionRequestDTO dto);
    
    /**
     * Remove um produto.
     */
    void delete(Long id);
    
    /**
     * Converte entidade para DTO de resposta.
     */
    PromotionResponseDTO toResponseDTO(T entity);
}
