package com.danrley.product_management.domain.grocery.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danrley.product_management.common.dto.product.ProductResponseDTO;
import com.danrley.product_management.common.dto.promotionCondition.PromotionConditionRequestDTO;
import com.danrley.product_management.core.model.BaseProduct;
import com.danrley.product_management.core.model.BasePromotion;
import com.danrley.product_management.core.model.BasePromotionCondition;
import com.danrley.product_management.domain.grocery.model.GroceryProduct;
import com.danrley.product_management.domain.grocery.model.GroceryPromotion;
import com.danrley.product_management.domain.grocery.model.GroceryPromotionCondition;
import com.danrley.product_management.domain.grocery.repository.GroceryPromotionConditionRepository;
import com.danrley.product_management.domain.grocery.repository.GroceryPromotionRepository;
import com.danrley.product_management.core.service.BasePromotionConditionService;


@Service
public class GroceryPromotionConditionService implements BasePromotionConditionService<GroceryPromotion, GroceryProduct, GroceryPromotionCondition> {

    private static final Logger logger = LoggerFactory.getLogger(GroceryPromotionConditionService.class);

    @Autowired
    private GroceryPromotionConditionRepository groceryPromotionConditionRepository;

    @Autowired
    private GroceryProductService groceryProductService;

    @Autowired
    private GroceryPromotionService groceryPromotionService;

    @Autowired
    private GroceryPromotionRepository groceryPromotionRepository;


    @Override
    public List<GroceryPromotion> createPromotion(PromotionConditionRequestDTO requestDTO) {
        try {
            List<Long> productIds = requestDTO.productIds;
            double discount = requestDTO.discountPercentage;

            // Busca todos os produtos pelos IDs
            List<GroceryProduct> groceryProducts = groceryProductService.getProductsByIds(productIds);

   

            // Cria promoções usando o PromotionService
            List<GroceryPromotion> promotions = groceryPromotionService.createPromotion(groceryProducts, discount);

            // Salva todas as promoções e retorna
            return groceryPromotionRepository.saveAll(promotions);

        } catch (Exception e) {
            logger.error("Erro ao criar promoções de grocery: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }


    @Override
    public GroceryPromotionCondition findById(Long id) {
        return groceryPromotionConditionRepository.findById(id).orElse(null);
    }


    @Override
    public void deleteById(Long id) {
        groceryPromotionConditionRepository.deleteById(id);
    }

    @Override
    public List<GroceryProduct> getEligibleProducts(List<GroceryProduct> products) {
        GroceryPromotionCondition groceryPromotionCondition = new GroceryPromotionCondition();
        if (products == null || products.isEmpty()) {
            return new ArrayList<>();
        }
        
        return products.stream()
                .filter(groceryPromotionCondition::isEligible) // <--- Aqui está a mudança!
                .collect(Collectors.toList());
    }

}
