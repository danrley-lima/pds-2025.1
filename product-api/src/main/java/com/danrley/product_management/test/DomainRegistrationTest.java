package com.danrley.product_management.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.danrley.product_management.framework.domain.DomainRegistry;

/**
 * Teste para verificar se todos os domínios estão registrados.
 */
@Component
@Order(4) // Executa após todos os registros, mas antes do seeder
public class DomainRegistrationTest implements CommandLineRunner {

    private final DomainRegistry domainRegistry;

    public DomainRegistrationTest(DomainRegistry domainRegistry) {
        this.domainRegistry = domainRegistry;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== TESTE DE DOMÍNIOS REGISTRADOS ===");
        System.out.println("Domínios registrados: " + domainRegistry.getRegisteredDomains().size());
        
        domainRegistry.getRegisteredDomains().forEach(domain -> {
            System.out.println("✅ Domínio: " + domain.getDisplayName() + " (" + domain.getCode() + ")");
        });
        
        System.out.println("Domínio padrão: " + domainRegistry.getDefaultConfiguration().getDomain().getDisplayName());
        System.out.println("=====================================\n");
    }
}
