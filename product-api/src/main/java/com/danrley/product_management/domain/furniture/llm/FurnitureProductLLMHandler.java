package com.danrley.product_management.domain.furniture.llm;

import org.springframework.stereotype.Service;

import com.danrley.product_management.core.domain.Domain;
import com.danrley.product_management.core.domain.DomainConfiguration;
import com.danrley.product_management.core.domain.DomainRegistry;
import com.danrley.product_management.core.llm.BaseLLMHandler;
import com.danrley.product_management.common.service.llm.provider.AIProviderFactory;

/**
 * Handler específico para busca de móveis no domínio de móveis e decoração.
 */
@Service
public class FurnitureProductLLMHandler extends BaseLLMHandler {

    private final DomainRegistry domainRegistry;

    public FurnitureProductLLMHandler(AIProviderFactory aiProviderFactory, DomainRegistry domainRegistry) {
        super(aiProviderFactory);
        this.domainRegistry = domainRegistry;
    }

    @Override
    protected String buildPrompt(String customerMessage, String productsStr, DomainConfiguration config) {
        String template = config.getProductSearchPromptTemplate();
        String jsonFormat = config.getProductResponseJsonFormat();
        
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
