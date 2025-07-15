package com.danrley.product_management.domain.furniture.llm;

import org.springframework.stereotype.Service;

import com.danrley.product_management.core.domain.Domain;
import com.danrley.product_management.core.domain.DomainConfiguration;
import com.danrley.product_management.core.domain.DomainRegistry;
import com.danrley.product_management.core.llm.BaseLLMHandler;
import com.danrley.product_management.common.service.llm.provider.AIProviderFactory;

/**
 * Handler para promoções de móveis.
 */
@Service
public class FurniturePromotionLLMHandler extends BaseLLMHandler {

    private final DomainRegistry domainRegistry;

    public FurniturePromotionLLMHandler(AIProviderFactory aiProviderFactory, DomainRegistry domainRegistry) {
        super(aiProviderFactory);
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
