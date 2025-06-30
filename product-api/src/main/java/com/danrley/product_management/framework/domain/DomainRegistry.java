package com.danrley.product_management.framework.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * Registry responsável por gerenciar todas as configurações de domínios disponíveis.
 * Permite registrar novos domínios e obter configurações específicas por domínio.
 */
@Component
public class DomainRegistry {

    private final Map<Domain, DomainConfiguration> configurations = new HashMap<>();
    private Domain defaultDomain = Domain.GROCERY; // Domínio padrão

    /**
     * Registra uma nova configuração de domínio.
     */
    public void register(DomainConfiguration configuration) {
        configurations.put(configuration.getDomain(), configuration);
    }

    /**
     * Obtém a configuração para um domínio específico.
     */
    public DomainConfiguration getConfiguration(Domain domain) {
        DomainConfiguration config = configurations.get(domain);
        if (config == null) {
            throw new IllegalArgumentException("No configuration found for domain: " + domain);
        }
        return config;
    }

    /**
     * Obtém a configuração do domínio padrão.
     */
    public DomainConfiguration getDefaultConfiguration() {
        return getConfiguration(defaultDomain);
    }

    /**
     * Define o domínio padrão.
     */
    public void setDefaultDomain(Domain domain) {
        this.defaultDomain = domain;
    }

    /**
     * Retorna todos os domínios registrados.
     */
    public Set<Domain> getRegisteredDomains() {
        return configurations.keySet();
    }

    /**
     * Verifica se um domínio está registrado.
     */
    public boolean isRegistered(Domain domain) {
        return configurations.containsKey(domain);
    }

    /**
     * Inicialização automática - registra as configurações padrão.
     */
    @PostConstruct
    public void initialize() {
        // As configurações específicas serão registradas pelos beans de cada domínio
    }
}
