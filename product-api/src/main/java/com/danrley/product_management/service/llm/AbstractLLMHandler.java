package com.danrley.product_management.service.llm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.danrley.product_management.dto.product.ProductResponseDTO;
import com.danrley.product_management.dto.recommendation.ProductNotFoundDTO;
import com.danrley.product_management.dto.recommendation.ProductOutDTO;
import com.danrley.product_management.service.llm.provider.AIProvider;
import com.danrley.product_management.service.llm.provider.AIProviderFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classe abstrata base para handlers de recomendação utilizando IA.
 * Define o contrato e funcionalidades comuns para diferentes tipos de recomendação.
 */
public abstract class AbstractLLMHandler {
    
    protected final AIProviderFactory aiProviderFactory;
    protected final ObjectMapper objectMapper;

    public AbstractLLMHandler(AIProviderFactory aiProviderFactory, ObjectMapper objectMapper) {
        this.aiProviderFactory = aiProviderFactory;
        this.objectMapper = objectMapper;
    }

    /**
     * Processa a mensagem do cliente e lista de produtos através da IA.
     * 
     * @param customerMessage Mensagem do cliente
     * @param products Lista de produtos disponíveis
     * @return Resultado da busca com produtos encontrados e não encontrados
     */
    public SearchResult searchProducts(String customerMessage, List<ProductResponseDTO> products) {
        try {
            String productsStr = formatProducts(products);
            String prompt = buildPrompt(customerMessage, productsStr);
            
            AIProvider provider = aiProviderFactory.getProvider();
            Map<String, Object> parameters = getAIParameters();
            String response = provider.generateContent(prompt, parameters);
            
            return parseResponse(response);
        } catch (Exception e) {
            return new SearchResult(new ArrayList<>(), new ArrayList<>());
        }
    }

    /**
     * Constrói o prompt específico para cada tipo de handler.
     * Deve ser implementado pelas subclasses.
     */
    protected abstract String buildPrompt(String customerMessage, String productsStr);

    /**
     * Define parâmetros específicos para o modelo de IA.
     * Subclasses podem sobrescrever para customizar comportamento.
     */
    protected Map<String, Object> getAIParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("temperature", 0.0);
        parameters.put("maxOutputTokens", 2000);
        return parameters;
    }

    protected String formatProducts(List<ProductResponseDTO> products) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < products.size(); i++) {
            ProductResponseDTO p = products.get(i);
            sb.append(p.id).append(",")
              .append(p.name).append(",")
              .append(p.brand).append(",")
              .append(p.categoryName).append(",")
              .append(p.unitWeight).append(" ").append(p.unitType).append(",")
              .append(p.unitPrice).append(",")
              .append(p.available).append(",")
              .append(p.onPromotion).append(",")
              .append(p.promotionalPrice).append(",")
              .append(p.stockQuantity).append(",")
              .append(p.priority);
            
            if (i < products.size() - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    protected SearchResult parseResponse(String response) {
        try {
            String cleanedResponse = cleanMarkdownResponse(response);
            JsonNode jsonNode = objectMapper.readTree(cleanedResponse);
            
            List<ProductOutDTO> products = new ArrayList<>();
            List<ProductNotFoundDTO> notFoundProducts = new ArrayList<>();
            
            JsonNode productsNode = jsonNode.get("products");
            if (productsNode != null && productsNode.isArray()) {
                for (JsonNode productNode : productsNode) {
                    ProductOutDTO product = objectMapper.treeToValue(productNode, ProductOutDTO.class);
                    products.add(product);
                }
            }
            // Processar produtos não encontrados
            JsonNode notFoundNode = jsonNode.get("notFoundProducts");
            if (notFoundNode == null) {
                notFoundNode = jsonNode.get("not_found_products");
            }
            
            if (notFoundNode != null && notFoundNode.isArray()) {
                for (JsonNode notFoundProductNode : notFoundNode) {
                    ProductNotFoundDTO notFound = objectMapper.treeToValue(notFoundProductNode, ProductNotFoundDTO.class);
                    notFoundProducts.add(notFound);
                }
            }
            
            return new SearchResult(products, notFoundProducts);
            
        } catch (Exception e) {
            return new SearchResult(new ArrayList<>(), new ArrayList<>());
        }
    }

    /**
     * Remove formatação markdown da resposta da IA
     */
    private String cleanMarkdownResponse(String response) {
        if (response == null) {
            return "";
        }
        
        // Remove ```json no início e ``` no final
        String cleaned = response.trim();
        
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7); // Remove "```json"
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3); // Remove "```"
        }
        
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3); // Remove "```" do final
        }
        
        return cleaned.trim();
    }

    /**
     * Classe para resultado da busca
     */
    public static class SearchResult {
        private final List<ProductOutDTO> products;
        private final List<ProductNotFoundDTO> notFoundProducts;

        public SearchResult(List<ProductOutDTO> products, List<ProductNotFoundDTO> notFoundProducts) {
            this.products = products;
            this.notFoundProducts = notFoundProducts;
        }

        public List<ProductOutDTO> getProducts() {
            return products;
        }

        public List<ProductNotFoundDTO> getNotFoundProducts() {
            return notFoundProducts;
        }
    }
}
