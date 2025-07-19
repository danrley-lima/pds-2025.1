package com.danrley.product_management.domain.construction.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.danrley.product_management.common.dto.promotion.PromotionResponseDTO;
import com.danrley.product_management.common.dto.promotionCondition.PromotionConditionRequestDTO;
import com.danrley.product_management.core.controller.BasePromotionConditionController;
import com.danrley.product_management.domain.construction.model.ConstructionProduct;
import com.danrley.product_management.domain.construction.model.ConstructionPromotion;
import com.danrley.product_management.domain.construction.model.ConstructionPromotionCondition;
import com.danrley.product_management.domain.construction.service.ConstructionPromotionConditionService;
import com.danrley.product_management.domain.construction.service.ConstructionPromotionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/construction/promotions")
public class ConstructionPromotionConditionController extends
        BasePromotionConditionController<ConstructionPromotion, ConstructionProduct, ConstructionPromotionCondition, ConstructionPromotionConditionService> {

    private final ConstructionPromotionConditionService promotionConditionService;
    private final ConstructionPromotionService promotionService;

    @Autowired
    public ConstructionPromotionConditionController(
            ConstructionPromotionConditionService promotionConditionService,
            ConstructionPromotionService promotionService) {
        super(promotionConditionService, promotionService);
        this.promotionConditionService = promotionConditionService;
        this.promotionService = promotionService;
    }

    @PostMapping("/conditions")
    @Operation(summary = "Criar promoções por categoria", description = "Cria promoções para todos os produtos de uma categoria com base no desconto informado.")
    public List<PromotionResponseDTO> create(
            @Parameter(description = "Dados da condição de promoção por categoria") @RequestBody PromotionConditionRequestDTO dto) {

        List<ConstructionPromotion> promotions = promotionConditionService.createPromotion(dto);
        return promotions.stream()
                .map(promotionService::toResponseDTO)
                .collect(Collectors.toList());
    }
}