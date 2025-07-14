package com.danrley.product_management.domain.grocery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danrley.product_management.common.dto.product.ProductResponseDTO;
import com.danrley.product_management.common.dto.recommendation.RecommendationRequestDTO;
import com.danrley.product_management.common.dto.recommendation.RecommendationResponseDTO;
import com.danrley.product_management.common.model.category.Category;
import com.danrley.product_management.common.repository.CategoryRepository;
import com.danrley.product_management.core.controller.BaseProductController;
import com.danrley.product_management.domain.grocery.model.GroceryProduct;
import com.danrley.product_management.domain.grocery.service.GroceryProductService;
import com.danrley.product_management.domain.grocery.service.GroceryRecommendationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller específico para o domínio de supermercado.
 * Estende BaseProductController para herdar operações CRUD comuns
 * e adiciona endpoints específicos do domínio grocery.
 */
@RestController
@RequestMapping("/api/grocery")
@Tag(name = "Grocery Domain", description = "API para produtos e funcionalidades do supermercado")
public class GroceryController extends BaseProductController<GroceryProduct, GroceryProductService> {

  @Autowired
  private GroceryRecommendationService recommendationService;

  @Autowired
  private CategoryRepository categoryRepository;

  public GroceryController(GroceryProductService productService) {
    super(productService);
  }

  @Override
  protected String getDomainName() {
    return "grocery";
  }

  // Endpoints do domínio

  @PostMapping("/recommendations")
  @Operation(summary = "Recomendações de produtos do supermercado", description = "Gera recomendações de produtos específicas para o domínio de supermercado")
  public ResponseEntity<RecommendationResponseDTO> getRecommendations(
      @RequestBody RecommendationRequestDTO request) {

    RecommendationResponseDTO response = recommendationService.getRecommendations(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/recipes")
  @Operation(summary = "Receitas e ingredientes", description = "Sugere receitas e lista ingredientes necessários")
  public ResponseEntity<RecommendationResponseDTO> getRecipes(
      @RequestBody RecommendationRequestDTO request) {

    RecommendationResponseDTO response = recommendationService.getRecipes(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/promotions")
  @Operation(summary = "Promoções de supermercado", description = "Encontra produtos em promoção no supermercado")
  public ResponseEntity<RecommendationResponseDTO> getPromotions(
      @RequestBody RecommendationRequestDTO request) {

    RecommendationResponseDTO response = recommendationService.getPromotions(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/priority")
  @Operation(summary = "Produtos prioritários", description = "Retorna produtos marcados como prioritários no supermercado")
  public ResponseEntity<List<ProductResponseDTO>> getPriorityProducts() {
    List<GroceryProduct> products = productService.getPriorityProducts();
    List<ProductResponseDTO> responseProducts = products.stream()
        .map(productService::toResponseDTO)
        .toList();
    return ResponseEntity.ok(responseProducts);
  }

  @GetMapping("/with-promotions")
  @Operation(summary = "Produtos em promoção", description = "Retorna produtos com promoções ativas")
  public ResponseEntity<List<ProductResponseDTO>> getProductsWithPromotions() {
    List<GroceryProduct> products = productService.getProductsWithActivePromotions();
    List<ProductResponseDTO> responseProducts = products.stream()
        .map(productService::toResponseDTO)
        .toList();
    return ResponseEntity.ok(responseProducts);
  }

  @GetMapping("/categories")
  @Operation(summary = "Lista categorias do supermercado", description = "Retorna todas as categorias disponíveis no supermercado")
  public ResponseEntity<List<Category>> getCategories() {
    List<Category> categories = categoryRepository.findAll();
    return ResponseEntity.ok(categories);
  }
}
