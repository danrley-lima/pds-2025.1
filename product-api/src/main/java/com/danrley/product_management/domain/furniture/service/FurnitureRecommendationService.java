package com.danrley.product_management.domain.furniture.service;

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
import com.danrley.product_management.framework.model.BaseProduct;
import com.danrley.product_management.service.llm.LLMClassifierService;
import com.danrley.product_management.domain.furniture.service.FurnitureProductService;
import com.danrley.product_management.domain.furniture.model.FurnitureProduct;
import com.danrley.product_management.domain.furniture.llm.FurnitureProductLLMHandler;
import com.danrley.product_management.domain.furniture.llm.FurnitureProjectLLMHandler;
import com.danrley.product_management.domain.furniture.llm.FurniturePromotionLLMHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Serviço específico para recomendações do domínio de móveis.
 * Herda funcionalidades comuns e implementa comportamentos específicos.
 */
@Service
public class FurnitureRecommendationService extends BaseRecommendationService {

    // Serviço específico para móveis
    private final FurnitureProductService furnitureProductService;

    // Handlers específicos para móveis
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
     * Projetos de decoração - método específico do domínio
     */
    public RecommendationResponseDTO getProjects(RecommendationRequestDTO request) {
        return getSpecialRecommendations(request, RequestCategory.RECIPE);
    }

    /**
     * Promoções de móveis - método específico do domínio
     */
    public RecommendationResponseDTO getPromotions(RecommendationRequestDTO request) {
        return getSpecialRecommendations(request, RequestCategory.SEARCH_PROMOTION);
    }

    // ========== IMPLEMENTAÇÕES DOS MÉTODOS ABSTRATOS ==========

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

    // ========== PARSERS ESPECÍFICOS DO DOMÍNIO ==========

    private void parseProjects(JsonNode projects, List<ProductOutDTO> foundProducts, 
                              List<ProductNotFoundDTO> notFoundProducts, Map<Long, BaseProduct> productMap) {
        for (JsonNode project : projects) {
            if (!project.has("items")) continue;
            
            for (JsonNode item : project.get("items")) {
                if (item.has("product_id")) {
                    // Item com ID de produto
                    Long productId = item.get("product_id").asLong();
                    String quantity = item.has("quantity") ? item.get("quantity").asText() : "1";
                    
                    BaseProduct product = productMap.get(productId);
                    if (product != null) {
                        foundProducts.add(createFromProduct(product, quantity));
                    }
                } else {
                    // Item não encontrado
                    String name = item.has("name") ? item.get("name").asText() : item.asText();
                    String quantity = item.has("quantity") ? item.get("quantity").asText() : "1";
                    notFoundProducts.add(new ProductNotFoundDTO(name, quantity));
                }
            }
        }
    }

}
