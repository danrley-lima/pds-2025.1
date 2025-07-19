package com.danrley.product_management.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.danrley.product_management.common.dto.product.ProductRequestDTO;
import com.danrley.product_management.common.dto.product.ProductResponseDTO;
import com.danrley.product_management.common.dto.promotion.PromotionResponseDTO;
import com.danrley.product_management.common.dto.promotionCondition.PromotionConditionRequestDTO;
import com.danrley.product_management.common.dto.promotionCondition.PromotionConditionResponseDTO;
import com.danrley.product_management.core.model.BaseProduct;
import com.danrley.product_management.core.model.BasePromotion;
import com.danrley.product_management.core.model.BasePromotionCondition;
import com.danrley.product_management.domain.grocery.model.GroceryPromotionCondition;

/**
 * Serviço base para condições de promoção.
 * Fornece funcionalidades comuns para avaliar elegibilidade de produtos para promoções.
 * 
 * @param <T> Tipo da entidade que implementa BaseProduct
 */
@Service
public interface BasePromotionConditionService<P extends BasePromotion, T extends BaseProduct, C extends BasePromotionCondition<T>> {

    public List<P> createPromotion(PromotionConditionRequestDTO products);

    public C findById(Long id);

    public void deleteById(Long id);

    public List<T> getEligibleProducts(List<T> products);

}