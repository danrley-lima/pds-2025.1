package com.danrley.product_management.core.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.danrley.product_management.common.dto.promotion.PromotionRequestDTO;
import com.danrley.product_management.common.dto.promotion.PromotionResponseDTO;
import com.danrley.product_management.core.model.BasePromotion;
import com.danrley.product_management.core.service.BasePromotionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public abstract class BasePromotionController<T extends BasePromotion, S extends BasePromotionService<T>> {
    protected final S promotionService;

    protected BasePromotionController(S promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as promoções", description = "Retorna todas as promoções do domínio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promoções listadas com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PromotionResponseDTO.class))))
    })
    public List<PromotionResponseDTO> getAll() {
        return promotionService.getAll().stream()
                .map(promotionService::toResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado", content = @Content(schema = @Schema(implementation = PromotionResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public PromotionResponseDTO getById(
            @Parameter(description = "ID da promoção") @PathVariable Long id) {
        T product = promotionService.getById(id)
                .orElseThrow(() -> new RuntimeException("Promoção não encontrada com ID: " + id));
        return promotionService.toResponseDTO(product);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar nova promoção", description = "Cria uma nova promoção no domínio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Promoção criada com sucesso", content = @Content(schema = @Schema(implementation = PromotionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public PromotionResponseDTO create(
            @Parameter(description = "Dados da promoção") @RequestBody PromotionRequestDTO dto) {
        T product = promotionService.create(dto);
        return promotionService.toResponseDTO(product);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar promoção", description = "Atualiza uma promoção existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promoção atualizada com sucesso", content = @Content(schema = @Schema(implementation = PromotionResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Promoção não encontrada")
    })
    public PromotionResponseDTO update(
            @Parameter(description = "ID da promoção") @PathVariable Long id,
            @Parameter(description = "Novos dados da promoção") @RequestBody PromotionRequestDTO dto) {
        T product = promotionService.update(id, dto);
        return promotionService.toResponseDTO(product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar promoção", description = "Remove uma promoção do domínio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Promoção deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Promoção não encontrada")
    })
    public void delete(
            @Parameter(description = "ID da promoção") @PathVariable Long id) {
        promotionService.delete(id);
    }
}
