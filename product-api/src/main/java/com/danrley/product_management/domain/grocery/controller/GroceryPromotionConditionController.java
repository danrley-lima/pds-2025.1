package com.danrley.product_management.domain.grocery.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danrley.product_management.common.dto.promotion.PromotionResponseDTO;
import com.danrley.product_management.common.dto.promotionCondition.PromotionConditionRequestDTO;
import com.danrley.product_management.core.controller.BasePromotionConditionController;
import com.danrley.product_management.domain.grocery.model.GroceryProduct;
import com.danrley.product_management.domain.grocery.model.GroceryPromotion;
import com.danrley.product_management.domain.grocery.model.GroceryPromotionCondition;
import com.danrley.product_management.domain.grocery.service.GroceryPromotionConditionService;
import com.danrley.product_management.domain.grocery.service.GroceryPromotionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/grocery/promotions")
public class GroceryPromotionConditionController extends
        BasePromotionConditionController<GroceryPromotion, GroceryProduct, GroceryPromotionCondition, GroceryPromotionConditionService> {

    private final GroceryPromotionConditionService promotionConditionService;
    private final GroceryPromotionService promotionService;

    @Autowired
    public GroceryPromotionConditionController(
            GroceryPromotionConditionService promotionConditionService,
            GroceryPromotionService promotionService) {
        super(promotionConditionService, promotionService);
        this.promotionConditionService = promotionConditionService;
        this.promotionService = promotionService;
    }

    @PostMapping("/conditions")
    @Operation(summary = "Criar promoções por produto", description = "Cria promoções para os produtos informados com base no desconto fornecido.")
    public List<PromotionResponseDTO> create(
            @Parameter(description = "Dados da condição de promoção por produto") @RequestBody PromotionConditionRequestDTO dto) {

        List<GroceryPromotion> promotions = promotionConditionService.createPromotion(dto);
        return promotions.stream()
                .map(promotionService::toResponseDTO)
                .collect(Collectors.toList());
    }
}
