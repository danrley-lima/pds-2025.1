package com.danrley.product_management.core.llm;

import com.danrley.product_management.core.domain.Domain;
import com.danrley.product_management.core.domain.DomainConfiguration;
import com.danrley.product_management.core.model.BaseProduct;
import com.danrley.product_management.common.service.llm.provider.AIProviderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Handler abstrato para processamento de produtos usando IA, com suporte a múltiplos domínios.
 * Subclasses devem implementar a lógica específica de cada domínio.
 */
public abstract class BaseLLMHandler {

    protected final AIProviderFactory aiProviderFactory;
    protected final ObjectMapper objectMapper;

    public BaseLLMHandler(AIProviderFactory aiProviderFactory, ObjectMapper objectMapper) {
        this.aiProviderFactory = aiProviderFactory;
        this.objectMapper = objectMapper;
    }

    /**
     * Processa uma mensagem do cliente usando a configuração do domínio especificado.
     */
    public String processRequest(String customerMessage, List<? extends BaseProduct> products, Domain domain) {
        DomainConfiguration config = getDomainConfiguration(domain);
        String productsStr = formatProductsForPrompt(products, config);
        String prompt = buildPrompt(customerMessage, productsStr, config);
        
        return callAI(prompt);
    }

    /**
     * Constrói o prompt usando a configuração do domínio.
     */
    protected abstract String buildPrompt(String customerMessage, String productsStr, DomainConfiguration config);

    /**
     * Obtém a configuração do domínio.
     */
    protected abstract DomainConfiguration getDomainConfiguration(Domain domain);

    /**
     * Formata a lista de produtos para uso no prompt.
     */
    protected String formatProductsForPrompt(List<? extends BaseProduct> products, DomainConfiguration config) {
        StringBuilder sb = new StringBuilder();
        for (BaseProduct product : products) {
            sb.append(config.formatProductForPrompt(product)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Realiza a chamada para o provedor de IA.
     */
    protected String callAI(String prompt) {
        try {
            return aiProviderFactory.getProvider().generateContent(prompt);
        } catch (Exception e) {
            throw new RuntimeException("Error calling AI provider: " + e.getMessage(), e);
        }
    }

    /**
     * Valida se a resposta da IA está no formato esperado.
     */
    protected boolean isValidResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            return false;
        }
        
        // Verifica se é um JSON válido
        try {
            objectMapper.readTree(response);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
