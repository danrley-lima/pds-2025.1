package com.danrley.product_management.core.domain;

import java.util.Set;

import org.springframework.context.ApplicationContext;

import com.danrley.product_management.core.model.BaseProduct;
import com.danrley.product_management.core.service.BaseProductService;
import com.danrley.product_management.core.service.BasePromotionService;
import com.danrley.product_management.core.service.BaseRecommendationService;

/**
 * Configuração concreta e completa de um domínio no framework.
 * Centraliza todas as informações necessárias para registrar um novo domínio.
 * Complementa a interface DomainConfiguration existente.
 */
public class CompleteDomainConfiguration {

    private final Domain domain;
    private final Class<? extends BaseProduct> productClass;
    private final Class<? extends BaseProductService<?>> productServiceClass;
    private final Class<? extends BasePromotionService<?>> promotionServiceClass;
    private final Class<? extends BaseRecommendationService> recommendationServiceClass;

    public CompleteDomainConfiguration(
            Domain domain,
            Class<? extends BaseProduct> productClass,
            Class<? extends BaseProductService<?>> productServiceClass,
            Class<? extends BasePromotionService<?>> promotionServiceClass,
            Class<? extends BaseRecommendationService> recommendationServiceClass) {

        this.domain = domain;
        this.productClass = productClass;
        this.productServiceClass = productServiceClass;
        this.promotionServiceClass = promotionServiceClass;
        this.recommendationServiceClass = recommendationServiceClass;
    }

    // Getters
    public Domain getDomain() {
        return domain;
    }

    public Class<? extends BaseProduct> getProductClass() {
        return productClass;
    }

    public Class<? extends BaseProductService<?>> getProductServiceClass() {
        return productServiceClass;
    }

    public Class<? extends BasePromotionService<?>> getPromotionServiceClass() {
        return promotionServiceClass;
    }

    public Class<? extends BaseRecommendationService> getRecommendationServiceClass() {
        return recommendationServiceClass;
    }

    public <T extends BaseProduct> BaseProductService<T> getProductService(ApplicationContext context) {
        return (BaseProductService<T>) context.getBean(productServiceClass);
    }

    public <T extends BaseProduct> BasePromotionService<T> getPromotionService(ApplicationContext context) {
        return (BasePromotionService<T>) context.getBean(promotionServiceClass);
    }

    /**
     * Obtém instância do serviço de recomendação do Spring Context.
     */
    public BaseRecommendationService getRecommendationService(ApplicationContext context) {
        return context.getBean(recommendationServiceClass);
    }

    /**
     * Builder para facilitar a criação de configurações de domínio.
     */
    public static class Builder {
        private Domain domain;
        private Class<? extends BaseProduct> productClass;
        private Class<? extends BaseProductService<?>> productServiceClass;
        private Class<? extends BasePromotionService<?>> promotionServiceClass;
        private Class<? extends BaseRecommendationService> recommendationServiceClass;

        public Builder domain(Domain domain) {
            this.domain = domain;
            return this;
        }

        public Builder productClass(Class<? extends BaseProduct> productClass) {
            this.productClass = productClass;
            return this;
        }

        public Builder productService(Class<? extends BaseProductService<?>> serviceClass) {
            this.productServiceClass = serviceClass;
            return this;
        }

        public Builder promotionService(Class<? extends BasePromotionService<?>> serviceClass) {
            this.promotionServiceClass = serviceClass;
            return this;
        }

        public Builder recommendationService(Class<? extends BaseRecommendationService> serviceClass) {
            this.recommendationServiceClass = serviceClass;
            return this;
        }
    }
}
