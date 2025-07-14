package com.danrley.product_management.domain.furniture.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.danrley.product_management.common.dto.recommendation.ProductNotFoundDTO;
import com.danrley.product_management.common.dto.recommendation.ProductOutDTO;
import com.danrley.product_management.common.dto.recommendation.RecommendationRequestDTO;
import com.danrley.product_management.common.dto.recommendation.RecommendationResponseDTO;
import com.danrley.product_management.common.enums.RequestCategory;
import com.danrley.product_management.common.service.llm.LLMClassifierService;
import com.danrley.product_management.core.domain.Domain;
import com.danrley.product_management.core.llm.BaseLLMHandler;
import com.danrley.product_management.core.model.BaseProduct;
import com.danrley.product_management.core.service.BaseRecommendationService;
import com.danrley.product_management.domain.furniture.llm.FurnitureProductLLMHandler;
import com.danrley.product_management.domain.furniture.llm.FurnitureProjectLLMHandler;
import com.danrley.product_management.domain.furniture.llm.FurniturePromotionLLMHandler;
import com.danrley.product_management.domain.furniture.model.FurnitureProduct;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Serviço de recomendações para móveis.
 * Herda funcionalidades comuns e adiciona comportamentos de móveis.
 */
@Service
public class FurnitureRecommendationService extends BaseRecommendationService {

  // Serviço de móveis
  private final FurnitureProductService furnitureProductService;

  // Handlers para móveis
  private final FurnitureProjectLLMHandler projectHandler;
  private final FurnitureProductLLMHandler productHandler;
  private final FurniturePromotionLLMHandler promotionHandler;

  public FurnitureRecommendationService(
      LLMClassifierService classifierService,
      FurnitureProductService furnitureProductService,
      ObjectMapper objectMapper,
      FurnitureProjectLLMHandler projectHandler,
      FurnitureProductLLMHandler productHandler,
      FurniturePromotionLLMHandler promotionHandler) {

    super(classifierService, objectMapper, Domain.FURNITURE);
    this.furnitureProductService = furnitureProductService;
    this.projectHandler = projectHandler;
    this.productHandler = productHandler;
    this.promotionHandler = promotionHandler;
  }

  /**
   * Projetos de decoração
   */
  public RecommendationResponseDTO getProjects(RecommendationRequestDTO request) {
    return getSpecialRecommendations(request, RequestCategory.RECIPE);
  }

  /**
   * Promoções de móveis
   */
  public RecommendationResponseDTO getPromotions(RecommendationRequestDTO request) {
    return getSpecialRecommendations(request, RequestCategory.SEARCH_PROMOTION);
  }

  // ========== MÉTODOS ABSTRATOS ==========

  @Override
  protected List<BaseProduct> getDomainProducts() {
    return furnitureProductService.getAll().stream()
        .map(dto -> {
          FurnitureProduct product = new FurnitureProduct();
          product.setId(dto.getId());
          product.setName(dto.getName());
          product.setBrand(dto.getBrand());
          product.setUnitPrice(dto.getUnitPrice());
          product.setStockQuantity(dto.getStockQuantity());
          product.setAvailable(dto.isAvailable());
          product.setPriority(dto.isPriority());
          return (BaseProduct) product;
        })
        .collect(Collectors.toList());
  }

  @Override
  protected BaseLLMHandler selectHandler(RequestCategory category) {
    return switch (category) {
      case RECIPE -> projectHandler; // "recipe" = projeto para móveis
      case SEARCH_PROMOTION -> promotionHandler;
      default -> productHandler;
    };
  }

  @Override
  protected void parseSpecificResponse(JsonNode jsonNode,
      List<ProductOutDTO> foundProducts,
      List<ProductNotFoundDTO> notFoundProducts,
      Map<Long, BaseProduct> productMap) {
    if (jsonNode.has("projects")) {
      parseProjects(jsonNode.get("projects"), foundProducts, notFoundProducts, productMap);
    } else if (jsonNode.has("products")) {
      parseProducts(jsonNode, foundProducts, notFoundProducts, productMap);
    } else {
      createFallback(foundProducts, jsonNode.toString());
    }
  }

  @Override
  protected String getDefaultCategoryName() {
    return "Móvel";
  }

  @Override
  protected String getDomainDisplayName() {
    return "Móveis";
  }

  // ========== PARSERS DO DOMÍNIO ==========

  private void parseProjects(JsonNode projects, List<ProductOutDTO> foundProducts,
      List<ProductNotFoundDTO> notFoundProducts, Map<Long, BaseProduct> productMap) {
    for (JsonNode project : projects) {
      if (project.has("items")) {
        for (JsonNode item : project.get("items")) {
          if (item.has("product_id")) {
            Long productId = item.get("product_id").asLong();
            String quantity = item.has("quantity") ? item.get("quantity").asText() : "1";

            BaseProduct product = productMap.get(productId);
            if (product != null) {
              foundProducts.add(createFromProduct(product, quantity));
            }
          } else {
            String name = item.has("name") ? item.get("name").asText() : item.asText();
            String quantity = item.has("quantity") ? item.get("quantity").asText() : "1";
            notFoundProducts.add(new ProductNotFoundDTO(name, quantity));
          }
        }
      }

      if (project.has("missing_items")) {
        for (JsonNode missing : project.get("missing_items")) {
          String name = missing.get("name").asText();
          String quantity = missing.has("quantity") ? missing.get("quantity").asText() : "1";
          notFoundProducts.add(new ProductNotFoundDTO(name, quantity));
        }
      }
    }
  }

}
