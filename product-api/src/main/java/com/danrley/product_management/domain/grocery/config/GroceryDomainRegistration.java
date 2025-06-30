package com.danrley.product_management.domain.grocery.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.danrley.product_management.domain.grocery.GroceryDomainConfiguration;
import com.danrley.product_management.framework.domain.DomainRegistry;

/**
 * Configuração que registra automaticamente o domínio de supermercado na inicialização.
 */
@Component
@Order(1) // Executa primeiro
public class GroceryDomainRegistration implements CommandLineRunner {

    private final DomainRegistry domainRegistry;
    private final GroceryDomainConfiguration groceryConfig;

    public GroceryDomainRegistration(DomainRegistry domainRegistry, 
                                   GroceryDomainConfiguration groceryConfig) {
        this.domainRegistry = domainRegistry;
        this.groceryConfig = groceryConfig;
    }

    @Override
    public void run(String... args) throws Exception {
        // Registra o domínio de supermercado
        domainRegistry.register(groceryConfig);
        
        // Define como domínio padrão
        domainRegistry.setDefaultDomain(groceryConfig.getDomain());
        
        System.out.println("Domínio de supermercado registrado com sucesso!");
    }
}
