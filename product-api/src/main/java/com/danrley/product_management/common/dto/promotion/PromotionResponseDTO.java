package com.danrley.product_management.common.dto.promotion;

import java.time.LocalDate;

import com.danrley.product_management.domain.construction.model.ConstructionPromotion;
import com.danrley.product_management.domain.grocery.model.GroceryPromotion;

public class PromotionResponseDTO {
    public Long id;
    public String description;
    public String productName;
    public double originalPrice;
    public double promotionalPrice;
    public LocalDate initialDate;
    public LocalDate finalDate;

    public static PromotionResponseDTO fromGroceryPromotion(GroceryPromotion promotion) {
        PromotionResponseDTO dto = new PromotionResponseDTO();
        mapBaseFields(promotion, dto);

        // Campos do grocery
        dto.productName = promotion.getGroceryProduct().getName();
        dto.originalPrice = promotion.getGroceryProduct().getUnitPrice();

        return dto;
    }

    public static PromotionResponseDTO fromConstructionPromotion(ConstructionPromotion promotion) {
        PromotionResponseDTO dto = new PromotionResponseDTO();
        mapBaseFields(promotion, dto);

        // Campos do construction
        dto.productName = promotion.getConstructionProduct().getName();
        dto.originalPrice = promotion.getConstructionProduct().getUnitPrice();

        return dto;
    }

    private static void mapBaseFields(Object promotion, PromotionResponseDTO dto) {
        // Mapeamento de campos base usando cast para BaseProduct
        if (promotion instanceof GroceryPromotion) {
            GroceryPromotion p = (GroceryPromotion) promotion;
            dto.id = p.getId();
            dto.description = p.getDescription();
            dto.promotionalPrice = p.getPromotionalPrice();
            dto.initialDate = p.getStartDate();
            dto.finalDate = p.getEndDate();
        } else if (promotion instanceof ConstructionPromotion) {
            ConstructionPromotion p = (ConstructionPromotion) promotion;
            dto.id = p.getId();
            dto.description = p.getDescription();
            dto.promotionalPrice = p.getPromotionalPrice();
            dto.initialDate = p.getStartDate();
            dto.finalDate = p.getEndDate();
        }
    }
}
