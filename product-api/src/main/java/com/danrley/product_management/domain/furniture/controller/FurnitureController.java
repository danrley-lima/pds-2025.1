package com.danrley.product_management.domain.furniture.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.danrley.product_management.domain.furniture.model.FurnitureProduct;
import com.danrley.product_management.domain.furniture.service.FurnitureProductService;
import com.danrley.product_management.domain.furniture.service.FurnitureRecommendationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller para produtos de móveis.
 * Herda operações CRUD básicas e adiciona funcionalidades de móveis e decoração.
 */
@RestController
@RequestMapping("/api/furniture")
@Tag(name = "Furniture Domain", description = "API para produtos de móveis e decoração")
public class FurnitureController extends BaseProductController<FurnitureProduct, FurnitureProductService> {

  @Autowired
  private FurnitureRecommendationService recommendationService;

  @Autowired
  private CategoryRepository categoryRepository;

  public FurnitureController(FurnitureProductService productService) {
    super(productService);
  }

  @Override
  protected String getDomainName() {
    return "furniture";
  }

  // ========== ENDPOINTS DO DOMÍNIO ==========

  @PostMapping("/recommendations")
  @Operation(summary = "Recomendações de móveis", description = "Gera recomendações de móveis baseadas em necessidades específicas")
  public ResponseEntity<RecommendationResponseDTO> getRecommendations(
      @RequestBody RecommendationRequestDTO request) {

    RecommendationResponseDTO response = recommendationService.getRecommendations(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/material/{material}")
  @Operation(summary = "Buscar por material", description = "Retorna produtos de um material específico")
  public ResponseEntity<List<ProductResponseDTO>> getByMaterial(
      @Parameter(description = "Material do móvel") @PathVariable String material) {
    List<FurnitureProduct> products = productService.getByMaterial(material);
    List<ProductResponseDTO> responseProducts = products.stream()
        .map(productService::toResponseDTO)
        .toList();
    return ResponseEntity.ok(responseProducts);
  }

  @GetMapping("/color/{color}")
  @Operation(summary = "Buscar por cor", description = "Retorna produtos de uma cor específica")
  public ResponseEntity<List<ProductResponseDTO>> getByColor(
      @Parameter(description = "Cor do móvel") @PathVariable String color) {
    List<FurnitureProduct> products = productService.getByColor(color);
    List<ProductResponseDTO> responseProducts = products.stream()
        .map(productService::toResponseDTO)
        .toList();
    return ResponseEntity.ok(responseProducts);
  }

  @GetMapping("/categories")
  @Operation(summary = "Lista categorias de móveis", description = "Retorna todas as categorias disponíveis para móveis")
  public ResponseEntity<List<Category>> getCategories() {
    List<Category> categories = categoryRepository.findAll();
    return ResponseEntity.ok(categories);
  }
}
