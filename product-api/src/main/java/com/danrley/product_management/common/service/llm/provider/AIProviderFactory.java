package com.danrley.product_management.common.service.llm.provider;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Factory simples para provedor de IA
 */
@Service
public class AIProviderFactory {

  private final AIProvider aiProvider;

  public AIProviderFactory(
      List<AIProvider> availableProviders,
      @Value("${ai.provider.preferred:gemini}") String preferredProvider) {

    // Busca o provedor preferido ou usa o primeiro configurado
    this.aiProvider = availableProviders.stream()
        .filter(p -> p.getProviderName().toLowerCase().contains(preferredProvider.toLowerCase()))
        .filter(AIProvider::isConfigured)
        .findFirst()
        .orElse(availableProviders.stream()
            .filter(AIProvider::isConfigured)
            .findFirst()
            .orElse(availableProviders.get(0))); // Fallback para o primeiro da lista
  }

  /**
   * Retorna o provedor de IA configurado
   */
  public AIProvider getProvider() {
    return aiProvider;
  }
}
