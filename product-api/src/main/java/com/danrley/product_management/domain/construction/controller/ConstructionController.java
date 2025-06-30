package com.danrley.product_management.domain.construction.controller;

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
import com.danrley.product_management.domain.construction.service.ConstructionProductService;
import com.danrley.product_management.domain.construction.service.ConstructionRecommendationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller específico para o domínio de material de construção.
 * Independente dos outros domínios.
 */
@RestController
@RequestMapping("/api/construction")
@Tag(name = "Construction Domain", description = "API específica para material de construção")
public class ConstructionController {

    private final ConstructionRecommendationService recommendationService;
    private final ConstructionProductService constructionProductService;
    private final CategoryRepository categoryRepository;

    public ConstructionController(ConstructionRecommendationService recommendationService,
                                ConstructionProductService constructionProductService,
                                CategoryRepository categoryRepository) {
        this.recommendationService = recommendationService;
        this.constructionProductService = constructionProductService;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/recommendations")
    @Operation(summary = "Recomendações de materiais", 
               description = "Busca materiais de construção")
    public ResponseEntity<RecommendationResponseDTO> getRecommendations(
            @RequestBody RecommendationRequestDTO request) {
        
        RecommendationResponseDTO response = recommendationService.getRecommendations(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/projects")
    @Operation(summary = "Projetos de construção", 
               description = "Gera sugestões de projetos de construção com materiais")
    public ResponseEntity<RecommendationResponseDTO> getProjects(
            @RequestBody RecommendationRequestDTO request) {
        
        RecommendationResponseDTO response = recommendationService.getProjects(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/promotions")
    @Operation(summary = "Promoções de materiais", 
               description = "Encontra materiais de construção em promoção")
    public ResponseEntity<RecommendationResponseDTO> getPromotions(
            @RequestBody RecommendationRequestDTO request) {
        
        RecommendationResponseDTO response = recommendationService.getPromotions(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products")
    @Operation(summary = "Lista materiais de construção", 
               description = "Retorna todos os materiais de construção disponíveis")
    public ResponseEntity<List<ProductResponseDTO>> getProducts() {
        List<ProductResponseDTO> products = constructionProductService.getAllAsDTO();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/categories")
    @Operation(summary = "Lista categorias de construção", 
               description = "Retorna todas as categorias de materiais de construção")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryRepository.findAll(); // Return all categories for now
        return ResponseEntity.ok(categories);
    }
}
