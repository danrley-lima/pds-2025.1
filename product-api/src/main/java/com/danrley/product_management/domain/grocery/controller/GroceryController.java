package com.danrley.product_management.domain.grocery.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danrley.product_management.dto.recommendation.RecommendationRequestDTO;
import com.danrley.product_management.dto.recommendation.RecommendationResponseDTO;
import com.danrley.product_management.model.category.Category;
import com.danrley.product_management.domain.grocery.model.GroceryProduct;
import com.danrley.product_management.domain.grocery.repository.GroceryProductRepository;
import com.danrley.product_management.repository.CategoryRepository;
import com.danrley.product_management.domain.grocery.service.GroceryRecommendationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller específico para o domínio de supermercado.
 * Independente dos outros domínios.
 */
@RestController
@RequestMapping("/api/grocery")
@Tag(name = "Grocery Domain", description = "API específica para supermercado")
public class GroceryController {

    private final GroceryRecommendationService recommendationService;
    private final GroceryProductRepository groceryProductRepository;
    private final CategoryRepository categoryRepository;

    public GroceryController(GroceryRecommendationService recommendationService,
                           GroceryProductRepository groceryProductRepository,
                           CategoryRepository categoryRepository) {
        this.recommendationService = recommendationService;
        this.groceryProductRepository = groceryProductRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/recommendations")
    @Operation(summary = "Recomendações de produtos do supermercado", 
               description = "Gera recomendações de produtos específicas para o domínio de supermercado")
    public ResponseEntity<RecommendationResponseDTO> getRecommendations(
            @RequestBody RecommendationRequestDTO request) {
        
        RecommendationResponseDTO response = recommendationService.getRecommendations(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recipes")
    @Operation(summary = "Receitas e ingredientes", 
               description = "Sugere receitas e lista ingredientes necessários")
    public ResponseEntity<RecommendationResponseDTO> getRecipes(
            @RequestBody RecommendationRequestDTO request) {
        
        RecommendationResponseDTO response = recommendationService.getRecipes(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/promotions")
    @Operation(summary = "Promoções de supermercado", 
               description = "Encontra produtos em promoção no supermercado")
    public ResponseEntity<RecommendationResponseDTO> getPromotions(
            @RequestBody RecommendationRequestDTO request) {
        
        RecommendationResponseDTO response = recommendationService.getPromotions(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products")
    @Operation(summary = "Lista produtos do supermercado", 
               description = "Retorna todos os produtos disponíveis no supermercado")
    public ResponseEntity<List<GroceryProduct>> getProducts() {
        List<GroceryProduct> products = groceryProductRepository.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/categories")
    @Operation(summary = "Lista categorias do supermercado", 
               description = "Retorna todas as categorias disponíveis no supermercado")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }
}
