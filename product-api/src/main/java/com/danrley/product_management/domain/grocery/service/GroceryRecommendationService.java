package com.danrley.product_management.domain.grocery.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.danrley.product_management.common.dto.recommendation.RecommendationRequestDTO;
import com.danrley.product_management.common.dto.recommendation.RecommendationResponseDTO;
import com.danrley.product_management.common.dto.recommendation.ProductOutDTO;
import com.danrley.product_management.common.dto.recommendation.ProductNotFoundDTO;
import com.danrley.product_management.common.enums.RequestCategory;
import com.danrley.product_management.common.service.llm.LLMClassifierService;
import com.danrley.product_management.core.domain.Domain;
import com.danrley.product_management.core.llm.BaseLLMHandler;
import com.danrley.product_management.core.model.BaseProduct;
import com.danrley.product_management.core.service.BaseRecommendationService;
import com.danrley.product_management.domain.grocery.llm.GroceryProductLLMHandler;
import com.danrley.product_management.domain.grocery.llm.GroceryRecipeLLMHandler;
import com.danrley.product_management.domain.grocery.llm.GroceryPromotionLLMHandler;
import com.danrley.product_management.domain.grocery.model.GroceryProduct;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Serviço de recomendações para supermercado.
 * Herda funcionalidades comuns e adiciona comportamentos de supermercado.
 */
@Service
public class GroceryRecommendationService extends BaseRecommendationService {

    // Core services
    private final GroceryProductService groceryProductService;

    // Handlers para supermercado
    private final GroceryRecipeLLMHandler recipeHandler;
    private final GroceryProductLLMHandler productHandler;
    private final GroceryPromotionLLMHandler promotionHandler;

    public GroceryRecommendationService(
            LLMClassifierService classifierService,
            GroceryProductService groceryProductService,
            ObjectMapper objectMapper,
            GroceryRecipeLLMHandler recipeHandler,
            GroceryProductLLMHandler productHandler,
            GroceryPromotionLLMHandler promotionHandler) {
        
        super(classifierService, objectMapper, Domain.GROCERY);
        this.groceryProductService = groceryProductService;
        this.recipeHandler = recipeHandler;
        this.productHandler = productHandler;
        this.promotionHandler = promotionHandler;
    }

    /**
     * Receitas e ingredientes
     */
    public RecommendationResponseDTO getRecipes(RecommendationRequestDTO request) {
        return getSpecialRecommendations(request, RequestCategory.RECIPE);
    }

    /**
     * Promoções do supermercado
     */
    public RecommendationResponseDTO getPromotions(RecommendationRequestDTO request) {
        return getSpecialRecommendations(request, RequestCategory.SEARCH_PROMOTION);
    }

    // ========== MÉTODOS ABSTRATOS ==========

    @Override
    protected List<BaseProduct> getDomainProducts() {
        return groceryProductService.getAll().stream()
            .map(dto -> {
                GroceryProduct product = new GroceryProduct();
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
            case RECIPE -> recipeHandler;
            case SEARCH_PROMOTION -> promotionHandler;
            default -> productHandler;
        };
    }

    @Override
    protected void parseSpecificResponse(JsonNode jsonNode, 
                                       List<ProductOutDTO> foundProducts, 
                                       List<ProductNotFoundDTO> notFoundProducts, 
                                       Map<Long, BaseProduct> productMap) {
        if (jsonNode.has("recipes")) {
            parseRecipes(jsonNode.get("recipes"), foundProducts, notFoundProducts, productMap);
        } else if (jsonNode.has("products")) {
            parseProducts(jsonNode, foundProducts, notFoundProducts, productMap);
        } else {
            createFallback(foundProducts, jsonNode.toString());
        }
    }

    @Override
    protected String getDefaultCategoryName() {
        return "Alimentício";
    }

    @Override
    protected String getDomainDisplayName() {
        return "Supermercado";
    }

    // ========== PARSERS DO DOMÍNIO ==========

    private void parseRecipes(JsonNode recipes, List<ProductOutDTO> foundProducts, 
                             List<ProductNotFoundDTO> notFoundProducts, Map<Long, BaseProduct> productMap) {
        for (JsonNode recipe : recipes) {
            if (!recipe.has("ingredients")) continue;
            
            for (JsonNode ingredient : recipe.get("ingredients")) {
                if (ingredient.has("product_id")) {
                    Long productId = ingredient.get("product_id").asLong();
                    String quantity = ingredient.has("quantity") ? ingredient.get("quantity").asText() : "1";
                    
                    BaseProduct product = productMap.get(productId);
                    if (product != null) {
                        foundProducts.add(createFromProduct(product, quantity));
                    }
                }
            }
            
            if (recipe.has("missing_ingredients")) {
                for (JsonNode missing : recipe.get("missing_ingredients")) {
                    String name = missing.get("name").asText();
                    String quantity = missing.has("quantity") ? missing.get("quantity").asText() : "1";
                    notFoundProducts.add(new ProductNotFoundDTO(name, quantity));
                }
            }
        }
    }
}
