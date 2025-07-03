package com.danrley.product_management.domain.grocery.llm;

import org.springframework.stereotype.Service;

import com.danrley.product_management.core.domain.Domain;
import com.danrley.product_management.core.domain.DomainConfiguration;
import com.danrley.product_management.core.domain.DomainRegistry;
import com.danrley.product_management.core.llm.BaseLLMHandler;
import com.danrley.product_management.common.service.llm.provider.AIProviderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handler específico para promoções no domínio de supermercado.
 */
@Service
public class GroceryPromotionLLMHandler extends BaseLLMHandler {

    private final DomainRegistry domainRegistry;

    public GroceryPromotionLLMHandler(AIProviderFactory aiProviderFactory, 
                                     ObjectMapper objectMapper,
                                     DomainRegistry domainRegistry) {
        super(aiProviderFactory, objectMapper);
        this.domainRegistry = domainRegistry;
    }

    @Override
    protected String buildPrompt(String customerMessage, String productsStr, DomainConfiguration config) {
        String template = config.getPromotionPromptTemplate();
        String jsonFormat = config.getProductResponseJsonFormat(); // Usa o mesmo formato de produtos
        
        return template
            .replace("{products}", productsStr)
            .replace("{json_format}", jsonFormat)
            .replace("{customer_message}", customerMessage);
    }

    @Override
    protected DomainConfiguration getDomainConfiguration(Domain domain) {
        return domainRegistry.getConfiguration(domain);
    }
}
