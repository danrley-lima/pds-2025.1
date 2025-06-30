package com.danrley.product_management.framework.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.danrley.product_management.dto.recommendation.RecommendationRequestDTO;
import com.danrley.product_management.dto.recommendation.RecommendationResponseDTO;
import com.danrley.product_management.dto.recommendation.ProductOutDTO;
import com.danrley.product_management.dto.recommendation.ProductNotFoundDTO;
import com.danrley.product_management.enums.RequestCategory;
import com.danrley.product_management.exception.custom.RecommendationException;
import com.danrley.product_management.framework.domain.Domain;
import com.danrley.product_management.framework.llm.BaseLLMHandler;
import com.danrley.product_management.framework.model.BaseProduct;
import com.danrley.product_management.service.llm.LLMClassifierService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classe base abstrata para serviços de recomendação.
 * Contém toda a lógica comum que pode ser reutilizada pelos domínios.
 * 
 * Template Method Pattern: Define o esqueleto do algoritmo,
 * permitindo que subclasses implementem comportamentos específicos.
 */
public abstract class BaseRecommendationService {

    protected final LLMClassifierService classifierService;
    protected final ObjectMapper objectMapper;
    protected final Domain domain;

    protected BaseRecommendationService(
            LLMClassifierService classifierService,
            ObjectMapper objectMapper,
            Domain domain) {
        this.classifierService = classifierService;
        this.objectMapper = objectMapper;
        this.domain = domain;
    }

    /**
     * Template method: Define o fluxo geral de processamento.
     * Subclasses implementam comportamentos específicos através de métodos abstratos.
     */
    public RecommendationResponseDTO getRecommendations(RecommendationRequestDTO request) {
        try {
            RequestCategory category = classifierService.classifyMessage(request.getCustomerMessage());
            return processRecommendation(request.getCustomerMessage(), category);
        } catch (Exception e) {
            throw new RecommendationException("Erro ao processar recomendação: " + e.getMessage(), e);
        }
    }

    /**
     * Método específico para cada tipo de recomendação
     */
    public RecommendationResponseDTO getSpecialRecommendations(RecommendationRequestDTO request, RequestCategory forcedCategory) {
        try {
            return processRecommendation(request.getCustomerMessage(), forcedCategory);
        } catch (Exception e) {
            throw new RecommendationException("Erro ao processar recomendação especial: " + e.getMessage(), e);
        }
    }

    /**
     * Processamento principal - reutilizável por todos os domínios
     */
    private RecommendationResponseDTO processRecommendation(String message, RequestCategory category) {
        List<BaseProduct> domainProducts = getDomainProducts();
        BaseLLMHandler handler = selectHandler(category);
        String aiResponse = handler.processRequest(message, domainProducts, domain);
        return parseResponse(aiResponse, domainProducts);
    }

    /**
     * Parser de resposta comum - reutilizável por todos os domínios
     */
    protected RecommendationResponseDTO parseResponse(String aiResponse, List<BaseProduct> products) {
        try {
            Map<Long, BaseProduct> productMap = createProductMap(products);
            JsonNode jsonNode = cleanAndParseJson(aiResponse);
            
            List<ProductOutDTO> foundProducts = new ArrayList<>();
            List<ProductNotFoundDTO> notFoundProducts = new ArrayList<>();
            
            // Delega parsing específico para subclasses
            parseSpecificResponse(jsonNode, foundProducts, notFoundProducts, productMap);
            
            return new RecommendationResponseDTO(foundProducts, notFoundProducts);
            
        } catch (Exception e) {
            System.err.println("Erro no parsing (" + domain + "): " + e.getMessage());
            return createErrorResponse(aiResponse);
        }
    }

    /**
     * Utilitários comuns - reutilizáveis
     */
    protected Map<Long, BaseProduct> createProductMap(List<BaseProduct> products) {
        return products.stream()
            .collect(Collectors.toMap(BaseProduct::getId, p -> p));
    }

    protected JsonNode cleanAndParseJson(String aiResponse) throws Exception {
        String clean = aiResponse.trim();
        if (clean.startsWith("```json")) clean = clean.substring(7);
        if (clean.endsWith("```")) clean = clean.substring(0, clean.length() - 3);
        return objectMapper.readTree(clean.trim());
    }

    protected ProductOutDTO createFromProduct(BaseProduct product, String quantity) {
        ProductOutDTO dto = new ProductOutDTO();
        dto.setId(String.valueOf(product.getId()));
        dto.setName(product.getName());
        dto.setUnitPrice(String.valueOf(product.getUnitPrice()));
        dto.setBrand(product.getBrand() != null ? product.getBrand() : "N/A");
        dto.setCategoryName(product.getCategory() != null ? 
            product.getCategory().getName() : getDefaultCategoryName());
        dto.setStockQuantity("Disponível");
        dto.setRequiredQuantity(quantity);
        return dto;
    }

    protected RecommendationResponseDTO createErrorResponse(String aiResponse) {
        List<ProductOutDTO> products = new ArrayList<>();
        ProductOutDTO fallback = new ProductOutDTO();
        fallback.setId(domain.name().toLowerCase() + "-error");
        fallback.setName("Resposta de " + getDomainDisplayName());
        fallback.setUnitPrice("0.00");
        fallback.setBrand("Sistema");
        fallback.setCategoryName("Erro");
        fallback.setStockQuantity(aiResponse.length() > 500 ? 
            aiResponse.substring(0, 500) + "..." : aiResponse);
        products.add(fallback);
        return new RecommendationResponseDTO(products, List.of());
    }

    /**
     * Parser comum para produtos
     */
    protected void parseProducts(JsonNode jsonNode, List<ProductOutDTO> foundProducts, 
                                List<ProductNotFoundDTO> notFoundProducts, Map<Long, BaseProduct> productMap) {
        for (JsonNode productNode : jsonNode.get("products")) {
            Long productId = productNode.get("id").asLong();
            BaseProduct product = productMap.get(productId);
            
            if (product != null) {
                String quantity = productNode.has("quantity") ? productNode.get("quantity").asText() : "1";
                foundProducts.add(createFromProduct(product, quantity));
            }
        }
        
        if (jsonNode.has("notFoundProducts")) {
            for (JsonNode notFound : jsonNode.get("notFoundProducts")) {
                String name = notFound.get("name").asText();
                String quantity = notFound.has("quantity") ? notFound.get("quantity").asText() : "1";
                notFoundProducts.add(new ProductNotFoundDTO(name, quantity));
            }
        }
    }

    /**
     * Criação de fallback comum - elimina duplicação entre domínios
     */
    protected void createFallback(List<ProductOutDTO> products, String aiResponse) {
        ProductOutDTO fallback = new ProductOutDTO();
        fallback.setId(domain.name().toLowerCase() + "-fallback");
        fallback.setName("Resposta de " + getDomainDisplayName());
        fallback.setUnitPrice("0.00");
        fallback.setBrand("Sistema");
        fallback.setCategoryName("Resposta");
        fallback.setStockQuantity(aiResponse.length() > 500 ? 
            aiResponse.substring(0, 500) + "..." : aiResponse);
        products.add(fallback);
    }

    // ========== MÉTODOS ABSTRATOS - IMPLEMENTADOS PELAS SUBCLASSES ==========

    /**
     * Cada domínio busca seus próprios produtos
     */
    protected abstract List<BaseProduct> getDomainProducts();

    /**
     * Cada domínio implementa sua lógica de seleção de handler
     */
    protected abstract BaseLLMHandler selectHandler(RequestCategory category);

    /**
     * Cada domínio implementa seu parser específico
     */
    protected abstract void parseSpecificResponse(JsonNode jsonNode, 
                                                List<ProductOutDTO> foundProducts, 
                                                List<ProductNotFoundDTO> notFoundProducts, 
                                                Map<Long, BaseProduct> productMap);

    /**
     * Nome padrão da categoria para cada domínio
     */
    protected abstract String getDefaultCategoryName();

    /**
     * Nome de exibição do domínio
     */
    protected abstract String getDomainDisplayName();
}
