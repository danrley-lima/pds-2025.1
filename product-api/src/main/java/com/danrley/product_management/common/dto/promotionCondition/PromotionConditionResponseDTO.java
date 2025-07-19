package com.danrley.product_management.common.dto.promotionCondition;

import java.util.List;

import com.danrley.product_management.common.dto.promotion.PromotionResponseDTO;
import com.danrley.product_management.domain.grocery.model.GroceryPromotion;

public class PromotionConditionResponseDTO {
    // public List<Long> productIds;
    // public String description;
    // public Integer daysBeforeExpiry; // para grocery

    // // Constructors
    // public PromotionConditionResponseDTO() {}

    // public PromotionConditionResponseDTO(List<Long> productIds, String description) {
    //     this.productIds = productIds;
    //     this.description = description;
    // }

    // public PromotionConditionResponseDTO(List<Long> productIds, String description, Integer daysBeforeExpiry) {
    //     this.productIds = productIds;
    //     this.description = description;
    //     this.daysBeforeExpiry = daysBeforeExpiry;
    // }

    // public static PromotionConditionResponseDTO fromGroceryPromotionCondition(GroceryPromotionCondition GroceryPromotionCondition) {
    //     PromotionConditionResponseDTO dto = new PromotionConditionResponseDTO();

    //     // Campos do grocery
    //     dto.productIds = List.of(promotionCondition.getGroceryProduct().getId());
    //     dto.description = promotionCondition.getDescription();
    //     dto.daysBeforeExpiry = promotionCondition.getDaysBeforeExpiry() != null ? promotionCondition.getDaysBeforeExpiry() : null;
    //     promotionCondition.getCategory() != null ? promotionCondition.getCategory().getName() : null;
    //     return dto;
    // }
}
