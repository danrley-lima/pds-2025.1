package com.danrley.product_management.core.service;

import java.util.List;

import com.danrley.product_management.common.dto.promotion.PromotionRequestDTO;
import com.danrley.product_management.common.dto.promotion.PromotionResponseDTO;
import com.danrley.product_management.core.model.BaseProduct;

/**
 * Interface base para serviços de promoção por domínio.
 * Permite que cada domínio gerencie suas próprias promoções.
 * 
 * @param <T> Tipo da entidade específica do domínio que implementa BaseProduct
 */
public interface BasePromotionService<T extends BaseProduct> {

  /**
   * Cria uma nova promoção para um produto do domínio.
   */
  PromotionResponseDTO createPromotion(Long productId, PromotionRequestDTO dto);

  /**
   * Lista todas as promoções do domínio.
   */
  List<PromotionResponseDTO> getAllPromotions();

  /**
   * Lista promoções ativas do domínio.
   */
  List<PromotionResponseDTO> getActivePromotions();

  /**
   * Lista promoções ativas para um produto específico.
   */
  List<PromotionResponseDTO> getActivePromotionsForProduct(Long productId);

  /**
   * Atualiza uma promoção existente.
   */
  PromotionResponseDTO updatePromotion(Long promotionId, PromotionRequestDTO dto);

  /**
   * Remove uma promoção.
   */
  void deletePromotion(Long promotionId);

  /**
   * Verifica se um produto tem promoções ativas.
   */
  boolean hasActivePromotions(Long productId);

  /**
   * Obtém o preço promocional atual de um produto.
   */
  Double getCurrentPromotionalPrice(Long productId);
}
