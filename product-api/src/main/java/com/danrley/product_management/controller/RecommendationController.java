package com.danrley.product_management.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danrley.product_management.dto.recommendation.RecommendationRequestDTO;
import com.danrley.product_management.dto.recommendation.RecommendationResponseDTO;
import com.danrley.product_management.service.RecommendationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/llm")
@Tag(name = "Recomendações LLM", description = "API para recomendações de produtos usando LLM")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping("/recommendations")
    @Operation(
        summary = "Obter recomendações de produtos", 
        description = "Recebe uma mensagem do cliente e retorna recomendações de produtos usando LLM"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Recomendações geradas com sucesso",
            content = @Content(schema = @Schema(implementation = RecommendationResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Dados da requisição inválidos"
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Erro interno do servidor"
        )
    })
    public ResponseEntity<RecommendationResponseDTO> getRecommendations(
            @Parameter(description = "Dados da requisição contendo mensagem do cliente e produtos disponíveis")
            @Valid @RequestBody RecommendationRequestDTO request) {
        
        try {
            RecommendationResponseDTO response = recommendationService.getRecommendations(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Em caso de erro, retorna erro interno
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
