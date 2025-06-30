package com.danrley.product_management.domain.grocery.llm;

import org.springframework.stereotype.Service;

import com.danrley.product_management.framework.domain.Domain;
import com.danrley.product_management.framework.domain.DomainConfiguration;
import com.danrley.product_management.framework.domain.DomainRegistry;
import com.danrley.product_management.framework.llm.BaseLLMHandler;
import com.danrley.product_management.service.llm.provider.AIProviderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handler específico para recomendações de receitas no domínio de supermercado.
 */
@Service
public class GroceryRecipeLLMHandler extends BaseLLMHandler {

    private final DomainRegistry domainRegistry;

    public GroceryRecipeLLMHandler(AIProviderFactory aiProviderFactory, 
                                  ObjectMapper objectMapper,
                                  DomainRegistry domainRegistry) {
        super(aiProviderFactory, objectMapper);
        this.domainRegistry = domainRegistry;
    }

    @Override
    protected String buildPrompt(String customerMessage, String productsStr, DomainConfiguration config) {
        String template = config.getRecipePromptTemplate();
        String jsonFormat = config.getRecipeResponseJsonFormat();
        
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
