package com.danrley.product_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.danrley.product_management.dto.product.ProductResponseDTO;
import com.danrley.product_management.dto.recommendation.RecommendationRequestDTO;
import com.danrley.product_management.dto.recommendation.RecommendationResponseDTO;
import com.danrley.product_management.enums.RequestCategory;
import com.danrley.product_management.exception.custom.RecommendationException;
import com.danrley.product_management.service.llm.AbstractLLMHandler;
import com.danrley.product_management.service.llm.LLMClassifierService;
import com.danrley.product_management.service.llm.ProductLLMHandler;
import com.danrley.product_management.service.llm.PromotionLLMHandler;
import com.danrley.product_management.service.llm.RecipeLLMHandler;

/**
 * Serviço responsável por processar recomendações de produtos utilizando IA.
 * Classifica a mensagem do cliente e delega para o handler apropriado.
 */
@Service
public class RecommendationService {

    private final LLMClassifierService classifierService;
    private final RecipeLLMHandler recipeLLMHandler;
    private final ProductLLMHandler productLLMHandler;
    private final PromotionLLMHandler promotionLLMHandler;
    private final ProductService productService;

    public RecommendationService(
            LLMClassifierService classifierService,
            RecipeLLMHandler recipeLLMHandler,
            ProductLLMHandler productLLMHandler,
            PromotionLLMHandler promotionLLMHandler,
            ProductService productService) {
        this.classifierService = classifierService;
        this.recipeLLMHandler = recipeLLMHandler;
        this.productLLMHandler = productLLMHandler;
        this.promotionLLMHandler = promotionLLMHandler;
        this.productService = productService;
    }

    /**
     * Processa uma requisição de recomendação de produtos.
     * 
     * @param request Requisição contendo a mensagem do cliente e opcionalmente produtos específicos
     * @return Resposta com produtos recomendados e produtos não encontrados
     * @throws RecommendationException Se ocorrer erro durante o processamento
     */
    public RecommendationResponseDTO getRecommendations(RecommendationRequestDTO request) {
        try {
            List<ProductResponseDTO> availableProducts = getAvailableProducts(request);
            RequestCategory category = classifierService.classifyMessage(request.getCustomerMessage());
            AbstractLLMHandler handler = selectHandler(category);
            
            AbstractLLMHandler.SearchResult result = handler.searchProducts(
                request.getCustomerMessage(), 
                availableProducts
            );

            return new RecommendationResponseDTO(result.getProducts(), result.getNotFoundProducts());

        } catch (Exception e) {
            throw new RecommendationException("Erro ao processar recomendação: " + e.getMessage(), e);
        }
    }

    private List<ProductResponseDTO> getAvailableProducts(RecommendationRequestDTO request) {
        List<ProductResponseDTO> products = request.getProducts();
        if (products == null || products.isEmpty()) {
            return productService.getAll();
        }
        return products;
    }

    private AbstractLLMHandler selectHandler(RequestCategory category) {
        switch (category) {
            case RECIPE:
                return recipeLLMHandler;
            case SEARCH_PRODUCT:
                return productLLMHandler;
            case SEARCH_PROMOTION:
                return promotionLLMHandler;
            default:
                return productLLMHandler;
        }
    }
}
