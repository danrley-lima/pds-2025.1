package com.danrley.product_management.domain.construction.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danrley.product_management.core.controller.BaseProductController;
import com.danrley.product_management.domain.construction.model.ConstructionProduct;
import com.danrley.product_management.domain.construction.service.ConstructionProductService;
import com.danrley.product_management.domain.construction.service.ConstructionRecommendationService;
import com.danrley.product_management.common.dto.product.ProductResponseDTO;
import com.danrley.product_management.common.dto.recommendation.RecommendationRequestDTO;
import com.danrley.product_management.common.dto.recommendation.RecommendationResponseDTO;
import com.danrley.product_management.common.model.category.Category;
import com.danrley.product_management.common.repository.CategoryRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller específico para o domínio de material de construção.
 * Estende BaseProductController para herdar operações CRUD comuns 
 * e adiciona endpoints específicos do domínio construction.
 */
@RestController
@RequestMapping("/api/construction")
@Tag(name = "Construction Domain", description = "API para produtos e funcionalidades de material de construção")
public class ConstructionController extends BaseProductController<ConstructionProduct, ConstructionProductService> {

    @Autowired
    private ConstructionRecommendationService recommendationService;
    
    @Autowired
    private CategoryRepository categoryRepository;

    public ConstructionController(ConstructionProductService productService) {
        super(productService);
    }
    
    @Override
    protected String getDomainName() {
        return "construction";
    }

    // ========== ENDPOINTS ESPECÍFICOS DO DOMÍNIO ==========

    @PostMapping("/recommendations")
    @Operation(summary = "Recomendações de materiais de construção", 
               description = "Gera recomendações de materiais baseadas em projeto de construção")
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
        
        // Usar o método padrão de recomendações por enquanto
        RecommendationResponseDTO response = recommendationService.getRecommendations(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/specifications/{specifications}")
    @Operation(summary = "Buscar por especificações", 
               description = "Retorna produtos com especificações específicas")
    public ResponseEntity<List<ProductResponseDTO>> getBySpecifications(
            @Parameter(description = "Especificações do material") @PathVariable String specifications) {
        List<ConstructionProduct> products = productService.getBySpecifications(specifications);
        List<ProductResponseDTO> responseProducts = products.stream()
                .map(productService::toResponseDTO)
                .toList();
        return ResponseEntity.ok(responseProducts);
    }

    @GetMapping("/application/{application}")
    @Operation(summary = "Buscar por aplicação", 
               description = "Retorna produtos para uma aplicação específica")
    public ResponseEntity<List<ProductResponseDTO>> getByApplication(
            @Parameter(description = "Aplicação do material") @PathVariable String application) {
        List<ConstructionProduct> products = productService.getByApplication(application);
        List<ProductResponseDTO> responseProducts = products.stream()
                .map(productService::toResponseDTO)
                .toList();
        return ResponseEntity.ok(responseProducts);
    }

    @GetMapping("/grade/{grade}")
    @Operation(summary = "Buscar por grau", 
               description = "Retorna produtos de um grau específico")
    public ResponseEntity<List<ProductResponseDTO>> getByGrade(
            @Parameter(description = "Grau do material") @PathVariable String grade) {
        List<ConstructionProduct> products = productService.getByGrade(grade);
        List<ProductResponseDTO> responseProducts = products.stream()
                .map(productService::toResponseDTO)
                .toList();
        return ResponseEntity.ok(responseProducts);
    }

    @GetMapping("/categories")
    @Operation(summary = "Lista categorias de construção", 
               description = "Retorna todas as categorias de materiais de construção")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }
}
