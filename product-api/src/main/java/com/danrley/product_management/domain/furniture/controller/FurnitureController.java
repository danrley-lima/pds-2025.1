package com.danrley.product_management.domain.furniture.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danrley.product_management.dto.recommendation.RecommendationRequestDTO;
import com.danrley.product_management.dto.recommendation.RecommendationResponseDTO;
import com.danrley.product_management.dto.product.ProductResponseDTO;
import com.danrley.product_management.model.category.Category;
import com.danrley.product_management.repository.CategoryRepository;
import com.danrley.product_management.domain.furniture.service.FurnitureProductService;
import com.danrley.product_management.domain.furniture.service.FurnitureRecommendationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller específico para o domínio de móveis e decoração.
 * Independente dos outros domínios.
 */
@RestController
@RequestMapping("/api/furniture")
@Tag(name = "Furniture Domain", description = "API específica para móveis e decoração")
public class FurnitureController {

    private final FurnitureRecommendationService recommendationService;
    private final FurnitureProductService furnitureProductService;
    private final CategoryRepository categoryRepository;

    public FurnitureController(FurnitureRecommendationService recommendationService,
                             FurnitureProductService furnitureProductService,
                             CategoryRepository categoryRepository) {
        this.recommendationService = recommendationService;
        this.furnitureProductService = furnitureProductService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/products")
    @Operation(summary = "Lista móveis e decoração", 
               description = "Retorna todos os móveis e itens de decoração disponíveis")
    public ResponseEntity<List<ProductResponseDTO>> getProducts() {
        List<ProductResponseDTO> products = furnitureProductService.getAllAsDTO();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/categories")
    @Operation(summary = "Lista categorias de móveis", 
               description = "Retorna todas as categorias de móveis e decoração")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryRepository.findAll(); // Return all categories for now
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/recommendations")
    @Operation(summary = "Recomendações de móveis", 
               description = "Busca móveis e gera projetos de decoração")
    public ResponseEntity<RecommendationResponseDTO> getFurnitureRecommendations(
            @RequestBody RecommendationRequestDTO request) {
        
        RecommendationResponseDTO response = recommendationService.getRecommendations(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/projects")
    @Operation(summary = "Projetos de decoração", 
               description = "Gera sugestões de projetos de decoração com móveis")
    public ResponseEntity<RecommendationResponseDTO> getProjects(
            @RequestBody RecommendationRequestDTO request) {
        
        RecommendationResponseDTO response = recommendationService.getProjects(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/promotions")
    @Operation(summary = "Promoções de móveis", 
               description = "Encontra móveis em promoção")
    public ResponseEntity<RecommendationResponseDTO> getPromotions(
            @RequestBody RecommendationRequestDTO request) {
        
        RecommendationResponseDTO response = recommendationService.getPromotions(request);
        return ResponseEntity.ok(response);
    }
}
