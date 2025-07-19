package com.danrley.product_management.domain.construction.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danrley.product_management.common.dto.promotionCondition.PromotionConditionRequestDTO;
import com.danrley.product_management.core.service.BasePromotionConditionService;
import com.danrley.product_management.domain.construction.model.ConstructionProduct;
import com.danrley.product_management.domain.construction.model.ConstructionPromotion;
import com.danrley.product_management.domain.construction.model.ConstructionPromotionCondition;
import com.danrley.product_management.domain.construction.repository.ConstructionProductRepository;
import com.danrley.product_management.domain.construction.repository.ConstructionPromotionConditionRepository;
import com.danrley.product_management.domain.construction.repository.ConstructionPromotionRepository;

@Service
public class ConstructionPromotionConditionService implements BasePromotionConditionService<ConstructionPromotion, ConstructionProduct, ConstructionPromotionCondition> {

    private static final Logger logger = LoggerFactory.getLogger(ConstructionPromotionConditionService.class);

    @Autowired
    private ConstructionPromotionConditionRepository constructionPromotionConditionRepository;

    @Autowired
    private ConstructionPromotionService constructionPromotionService;

    @Autowired
    private ConstructionPromotionRepository constructionPromotionRepository;

    @Autowired
    private ConstructionProductRepository constructionProductRepository;

    @Override
    public List<ConstructionPromotion> createPromotion(PromotionConditionRequestDTO requestDTO) {
        try {
            Long categoryId = requestDTO.categoryId;
            double discount = requestDTO.discountPercentage;

            // Buscar produtos da categoria
            List<ConstructionProduct> products = constructionProductRepository.findByCategoryId(categoryId);
            if (products.isEmpty()) {
                logger.warn("Nenhum produto encontrado para a categoria ID: {}", categoryId);
                return Collections.emptyList();
            }

            // Criar promoções
            List<ConstructionPromotion> promotions = constructionPromotionService.createPromotion(categoryId, discount);

            // Salvar promoções e retornar
            return constructionPromotionRepository.saveAll(promotions);

        } catch (Exception e) {
            logger.error("Erro ao criar promoções de construção: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public ConstructionPromotionCondition findById(Long id) {
        return constructionPromotionConditionRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        constructionPromotionConditionRepository.deleteById(id);
    }

    @Override
    public List<ConstructionProduct> getEligibleProducts(List<ConstructionProduct> products) {
        ConstructionPromotionCondition condition = new ConstructionPromotionCondition();
        if (products == null || products.isEmpty()) {
            return new ArrayList<>();
        }

        return products.stream()
                .filter(condition::isEligible)
                .collect(Collectors.toList());
    }
}

