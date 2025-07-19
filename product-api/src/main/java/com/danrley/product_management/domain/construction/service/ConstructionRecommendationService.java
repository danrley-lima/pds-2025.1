package com.danrley.product_management.domain.construction.service;

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
import com.danrley.product_management.domain.construction.llm.ConstructionProductLLMHandler;
import com.danrley.product_management.domain.construction.llm.ConstructionProjectLLMHandler;
import com.danrley.product_management.domain.construction.llm.ConstructionPromotionLLMHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Serviço de recomendações para materiais de construção.
 * Herda funcionalidades comuns e adiciona comportamentos de construção.
 */
@Service
public class ConstructionRecommendationService extends BaseRecommendationService {

  // Serviço de construção
  private final ConstructionProductService constructionProductService;

  // Handlers para construção
  private final ConstructionProjectLLMHandler projectHandler;
  private final ConstructionProductLLMHandler productHandler;
  private final ConstructionPromotionLLMHandler promotionHandler;

  public ConstructionRecommendationService(
      LLMClassifierService classifierService,
      ConstructionProductService constructionProductService,
      ObjectMapper objectMapper,
      ConstructionProjectLLMHandler projectHandler,
      ConstructionProductLLMHandler productHandler,
      ConstructionPromotionLLMHandler promotionHandler) {

    super(classifierService, objectMapper, Domain.CONSTRUCTION);
    this.constructionProductService = constructionProductService;
    this.projectHandler = projectHandler;
    this.productHandler = productHandler;
    this.promotionHandler = promotionHandler;
  }

  /**
   * Projetos de construção
   */
  public RecommendationResponseDTO getProjects(RecommendationRequestDTO request) {
    return getSpecialRecommendations(request, RequestCategory.RECIPE);
  }

  /**
   * Promoções de materiais de construção
   */
  public RecommendationResponseDTO getPromotions(RecommendationRequestDTO request) {
    return getSpecialRecommendations(request, RequestCategory.SEARCH_PROMOTION);
  }

  // ========== MÉTODOS ABSTRATOS ==========

  @Override
  protected BaseLLMHandler selectHandler(RequestCategory category) {
    return switch (category) {
      case RECIPE -> projectHandler; // "recipe" = projeto para construção
      case SEARCH_PROMOTION -> promotionHandler;
      default -> productHandler;
    };
  }

  @Override
  protected void parseSpecificResponse(JsonNode jsonNode,
      List<ProductOutDTO> foundProducts,
      List<ProductNotFoundDTO> notFoundProducts,
      Map<Long, BaseProduct> productMap) {
    if (jsonNode.has("products")) {
      parseProducts(jsonNode.get("products"), foundProducts, notFoundProducts, productMap);
    } else {
      createFallback(foundProducts, jsonNode.toString());
    }
  }

  @Override
  protected String getDefaultCategoryName() {
    return "Material";
  }

  @Override
  protected String getDomainDisplayName() {
    return "Construção";
  }

  @Override
  protected List<BaseProduct> getDomainProducts() {
    return constructionProductService.getAll().stream()
        .map(BaseProduct.class::cast)
        .collect(Collectors.toList());
  }

  // ========== PARSERS DO DOMÍNIO ==========

  private void parseProjects(JsonNode projects, List<ProductOutDTO> foundProducts,
      List<ProductNotFoundDTO> notFoundProducts, Map<Long, BaseProduct> productMap) {
    for (JsonNode project : projects) {
      if (project.has("materials")) {
        for (JsonNode material : project.get("materials")) {
          if (material.has("product_id")) {
            Long productId = material.get("product_id").asLong();
            String quantity = material.has("quantity") ? material.get("quantity").asText() : "1";

            BaseProduct product = productMap.get(productId);
            if (product != null) {
              foundProducts.add(createFromProduct(product, quantity));
            }
          } else {
            String name = material.has("name") ? material.get("name").asText() : material.asText();
            String quantity = material.has("quantity") ? material.get("quantity").asText() : "1";
            notFoundProducts.add(new ProductNotFoundDTO(name, quantity));
          }
        }
      }

      if (project.has("missing_materials")) {
        for (JsonNode missing : project.get("missing_materials")) {
          String name = missing.get("name").asText();
          String quantity = missing.has("quantity") ? missing.get("quantity").asText() : "1";
          notFoundProducts.add(new ProductNotFoundDTO(name, quantity));
        }
      }
    }
  }

}
