package com.danrley.product_management.core.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.danrley.product_management.core.model.BaseProduct;
import com.danrley.product_management.core.model.BasePromotionCondition;

/**
 * Serviço base para condições de promoção.
 * Fornece funcionalidades comuns para avaliar elegibilidade de produtos para promoções.
 * 
 * @param <T> Tipo da entidade que implementa BaseProduct
 */
@Service
public interface BasePromotionConditionService<T extends BaseProduct> {

    public BasePromotionCondition<T> createPromotion(List<BaseProduct> products);

    public BasePromotionCondition<T> findById(Long id);

    public void deleteById(Long id);



    public List<T> getEligibleProducts(List<T> products);

}