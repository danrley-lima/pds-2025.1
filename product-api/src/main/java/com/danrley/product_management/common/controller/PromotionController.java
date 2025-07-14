package com.danrley.product_management.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.danrley.product_management.common.dto.promotion.PromotionRequestDTO;
import com.danrley.product_management.common.dto.promotion.PromotionResponseDTO;
import com.danrley.product_management.common.service.PromotionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/promotions")
@Tag(name = "Promoções", description = "API para controle de promoções de produtos")
public class PromotionController {
  @Autowired
  private PromotionService promotionService;

  @GetMapping
  @Operation(summary = "Listar todas as promoções", description = "Retorna todas as promoções cadastradas no sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Promoções listadas com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PromotionResponseDTO.class)))),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public List<PromotionResponseDTO> getAll() {
    return promotionService.getAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar promoção por ID", description = "Retorna uma única promoção com base no ID fornecido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Promoção encontrada com sucesso", content = @Content(schema = @Schema(implementation = PromotionResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Promoção não encontrada"),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public PromotionResponseDTO getById(
      @Parameter(description = "ID da promoção a ser buscada") @PathVariable Long id) {
    return promotionService.getById(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Excluir promoção", description = "Remove uma promoção com base no ID fornecido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Promoção excluída com sucesso"),
      @ApiResponse(responseCode = "404", description = "Promoção não encontrada"),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public void delete(
      @Parameter(description = "ID da promoção a ser excluída") @PathVariable Long id) {
    promotionService.delete(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Criar nova promoção", description = "Cria uma nova promoção para um produto de qualquer domínio")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Promoção criada com sucesso", content = @Content(schema = @Schema(implementation = PromotionResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public PromotionResponseDTO create(
      @Parameter(description = "Dados da promoção a ser criada (incluir campo 'domain': grocery/furniture/construction)") @RequestBody PromotionRequestDTO dto) {
    return promotionService.create(dto);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar promoção", description = "Atualiza uma promoção existente com base no ID e dados fornecidos")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Promoção atualizada com sucesso", content = @Content(schema = @Schema(implementation = PromotionResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
      @ApiResponse(responseCode = "404", description = "Promoção não encontrada"),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public PromotionResponseDTO update(
      @Parameter(description = "ID da promoção a ser atualizada") @PathVariable Long id,
      @Parameter(description = "Novos dados da promoção") @RequestBody PromotionRequestDTO dto) {
    return promotionService.update(id, dto);
  }

  @GetMapping("/active")
  @Operation(summary = "Listar promoções ativas", description = "Retorna todas as promoções que estão ativas na data atual")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Promoções ativas listadas com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PromotionResponseDTO.class)))),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public List<PromotionResponseDTO> getActivePromotions() {
    return promotionService.getActivePromotions();
  }
}