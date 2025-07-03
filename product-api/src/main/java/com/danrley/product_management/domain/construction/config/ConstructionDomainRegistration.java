package com.danrley.product_management.domain.construction.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.danrley.product_management.core.domain.DomainRegistry;
import com.danrley.product_management.domain.construction.ConstructionDomainConfiguration;

/**
 * Configuração que registra automaticamente o domínio de construção na inicialização.
 */
@Component
@Order(3) // Executa após furniture
public class ConstructionDomainRegistration implements CommandLineRunner {

    private final DomainRegistry domainRegistry;
    private final ConstructionDomainConfiguration constructionConfig;

    public ConstructionDomainRegistration(DomainRegistry domainRegistry, 
                                        ConstructionDomainConfiguration constructionConfig) {
        this.domainRegistry = domainRegistry;
        this.constructionConfig = constructionConfig;
    }

    @Override
    public void run(String... args) throws Exception {
        // Registra o domínio de construção
        domainRegistry.register(constructionConfig);
        System.out.println("✅ Domínio CONSTRUCTION registrado");
    }
}
