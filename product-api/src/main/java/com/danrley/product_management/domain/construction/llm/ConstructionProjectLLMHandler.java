package com.danrley.product_management.domain.construction.llm;

import org.springframework.stereotype.Service;

import com.danrley.product_management.core.domain.Domain;
import com.danrley.product_management.core.domain.DomainConfiguration;
import com.danrley.product_management.core.domain.DomainRegistry;
import com.danrley.product_management.core.llm.BaseLLMHandler;
import com.danrley.product_management.common.service.llm.provider.AIProviderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handler específico para etapas de construção e projetos.
 */
@Service
public class ConstructionProjectLLMHandler extends BaseLLMHandler {

    private final DomainRegistry domainRegistry;

    public ConstructionProjectLLMHandler(AIProviderFactory aiProviderFactory, 
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
