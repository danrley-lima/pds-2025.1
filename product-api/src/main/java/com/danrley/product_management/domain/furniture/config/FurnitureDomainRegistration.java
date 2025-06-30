package com.danrley.product_management.domain.furniture.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.danrley.product_management.domain.furniture.FurnitureDomainConfiguration;
import com.danrley.product_management.framework.domain.DomainRegistry;

/**
 * Configuração que registra automaticamente o domínio de móveis na inicialização.
 */
@Component
@Order(2) // Executa após grocery
public class FurnitureDomainRegistration implements CommandLineRunner {

    private final DomainRegistry domainRegistry;
    private final FurnitureDomainConfiguration furnitureConfig;

    public FurnitureDomainRegistration(DomainRegistry domainRegistry, 
                                     FurnitureDomainConfiguration furnitureConfig) {
        this.domainRegistry = domainRegistry;
        this.furnitureConfig = furnitureConfig;
    }

    @Override
    public void run(String... args) throws Exception {
        // Registra o domínio de móveis
        domainRegistry.register(furnitureConfig);
        System.out.println("✅ Domínio FURNITURE registrado");
    }
}
