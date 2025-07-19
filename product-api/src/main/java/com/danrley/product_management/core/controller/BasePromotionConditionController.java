package com.danrley.product_management.core.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.danrley.product_management.common.dto.product.ProductRequestDTO;
import com.danrley.product_management.common.dto.product.ProductResponseDTO;
import com.danrley.product_management.common.dto.promotion.PromotionResponseDTO;
import com.danrley.product_management.common.dto.promotionCondition.PromotionConditionRequestDTO;
import com.danrley.product_management.common.dto.promotionCondition.PromotionConditionResponseDTO;
import com.danrley.product_management.core.model.BaseProduct;
import com.danrley.product_management.core.model.BasePromotion;
import com.danrley.product_management.core.model.BasePromotionCondition;
import com.danrley.product_management.core.service.BaseProductService;
import com.danrley.product_management.core.service.BasePromotionConditionService;
import com.danrley.product_management.core.service.BasePromotionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public abstract class BasePromotionConditionController<P extends BasePromotion, T extends BaseProduct, C extends BasePromotionCondition<T>, S extends BasePromotionConditionService<P, T, C>> {

    protected final S promotionConditionService;

    protected final BasePromotionService<P> promotionService;

    public BasePromotionConditionController(S promotionConditionService, BasePromotionService<P> promotionService) {
        this.promotionConditionService = promotionConditionService;
        this.promotionService = promotionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar nova condição de promoção", description = "Cria uma nova condição de promoção")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Condição de promoção criada com sucesso", content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public List<PromotionResponseDTO> create(
            @Parameter(description = "Lista de IDs dos produtos") @RequestBody PromotionConditionRequestDTO dto) {

        List<P> promotionCondition = promotionConditionService.createPromotion(dto);
        return promotionCondition.stream()
        .map(promotionService::toResponseDTO)
        .collect(Collectors.toList());
    }

}
