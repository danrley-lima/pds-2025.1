package com.danrley.product_management.domain.construction.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.danrley.product_management.dto.recommendation.RecommendationRequestDTO;
import com.danrley.product_management.dto.recommendation.RecommendationResponseDTO;
import com.danrley.product_management.dto.recommendation.ProductOutDTO;
import com.danrley.product_management.dto.recommendation.ProductNotFoundDTO;
import com.danrley.product_management.enums.RequestCategory;
import com.danrley.product_management.framework.domain.Domain;
import com.danrley.product_management.framework.llm.BaseLLMHandler;
import com.danrley.product_management.framework.service.BaseRecommendationService;
import com.danrley.product_management.service.llm.LLMClassifierService;
import com.danrley.product_management.domain.construction.service.ConstructionProductService;
import com.danrley.product_management.domain.construction.llm.ConstructionProductLLMHandler;
import com.danrley.product_management.domain.construction.llm.ConstructionProjectLLMHandler;
import com.danrley.product_management.domain.construction.llm.ConstructionPromotionLLMHandler;
import com.danrley.product_management.framework.model.BaseProduct;
import com.danrley.product_management.domain.construction.model.ConstructionProduct;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Serviço específico para recomendações do domínio de construção.
 * Herda funcionalidades comuns e implementa comportamentos específicos.
 */
@Service
public class ConstructionRecommendationService extends BaseRecommendationService {

    // Serviço específico para construção
    private final ConstructionProductService constructionProductService;

    // Handlers específicos para construção
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
     * Projetos de construção - método específico do domínio
     */
    public RecommendationResponseDTO getProjects(RecommendationRequestDTO request) {
        return getSpecialRecommendations(request, RequestCategory.RECIPE);
    }

    /**
     * Promoções de materiais de construção - método específico do domínio
     */
    public RecommendationResponseDTO getPromotions(RecommendationRequestDTO request) {
        return getSpecialRecommendations(request, RequestCategory.SEARCH_PROMOTION);
    }

    // ========== IMPLEMENTAÇÕES DOS MÉTODOS ABSTRATOS ==========

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

    // ========== PARSERS ESPECÍFICOS DO DOMÍNIO ==========

    private void parseProjects(JsonNode projects, List<ProductOutDTO> foundProducts, 
                              List<ProductNotFoundDTO> notFoundProducts, Map<Long, BaseProduct> productMap) {
        for (JsonNode project : projects) {
            if (!project.has("materials")) continue;
            
            for (JsonNode material : project.get("materials")) {
                if (material.has("product_id")) {
                    // Material com ID de produto
                    Long productId = material.get("product_id").asLong();
                    String quantity = material.has("quantity") ? material.get("quantity").asText() : "1";
                    
                    BaseProduct product = productMap.get(productId);
                    if (product != null) {
                        foundProducts.add(createFromProduct(product, quantity));
                    }
                } else {
                    // Material não encontrado
                    String name = material.has("name") ? material.get("name").asText() : material.asText();
                    String quantity = material.has("quantity") ? material.get("quantity").asText() : "1";
                    notFoundProducts.add(new ProductNotFoundDTO(name, quantity));
                }
            }
        }
    }

}
