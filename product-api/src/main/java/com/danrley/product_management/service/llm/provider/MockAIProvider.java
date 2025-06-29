package com.danrley.product_management.service.llm.provider;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Mock Provider para desenvolvimento e testes
 * Ativado quando ai.provider.mock.enabled=true
 */
@Service
@ConditionalOnProperty(name = "ai.provider.mock.enabled", havingValue = "true")
public class MockAIProvider implements AIProvider {

    private static final String PROVIDER_NAME = "Mock AI Provider";

    @Override
    public String generateContent(String prompt, Map<String, Object> parameters) {
        // Simula diferentes respostas baseadas no prompt
        if (prompt.toLowerCase().contains("recipe") || prompt.toLowerCase().contains("receita")) {
            return getMockRecipeResponse();
        } else if (prompt.toLowerCase().contains("product") || prompt.toLowerCase().contains("produto")) {
            return getMockProductResponse();
        } else if (prompt.toLowerCase().contains("promotion") || prompt.toLowerCase().contains("promoção")) {
            return getMockPromotionResponse();
        } else if (prompt.toLowerCase().contains("classify") || prompt.toLowerCase().contains("classificar")) {
            return "RECIPE";
        } else {
            return getMockGenericResponse();
        }
    }

    @Override
    public Map<String, Object> getDefaultParameters() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("temperature", 0.0);
        defaults.put("maxOutputTokens", 1000);
        return defaults;
    }

    @Override
    public boolean isConfigured() {
        return true; // Mock sempre está "configurado"
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public ProviderLimits getLimits() {
        // Limites "ilimitados" para mock
        return new ProviderLimits(
            10000, // maxTokensPerRequest
            1000,  // requestsPerMinute  
            50000  // requestsPerDay
        );
    }

    private String getMockRecipeResponse() {
        return """
        {
          "products": [
            {
              "id": "1",
              "name": "Farinha de Trigo",
              "brand": "Marca A",
              "categoryName": "Cereais",
              "unitPrice": "5.50",
              "stockQuantity": "100",
              "requiredQuantity": "2"
            },
            {
              "id": "2",
              "name": "Açúcar",
              "brand": "Marca B", 
              "categoryName": "Açúcares",
              "unitPrice": "3.20",
              "stockQuantity": "50",
              "requiredQuantity": "1"
            }
          ],
          "notFoundProducts": [
            {
              "name": "Chocolate em Pó",
              "quantity": "200g"
            }
          ]
        }
        """;
    }

    private String getMockProductResponse() {
        return """
        {
          "products": [
            {
              "id": "3",
              "name": "Leite Integral",
              "brand": "Marca C",
              "categoryName": "Laticínios",
              "unitPrice": "4.50",
              "stockQuantity": "30",
              "requiredQuantity": "1"
            }
          ],
          "notFoundProducts": []
        }
        """;
    }

    private String getMockPromotionResponse() {
        return """
        {
          "products": [
            {
              "id": "4",
              "name": "Biscoito",
              "brand": "Marca D",
              "categoryName": "Biscoitos",
              "unitPrice": "2.50",
              "promotionalPrice": "1.99",
              "stockQuantity": "20",
              "requiredQuantity": "1"
            }
          ],
          "notFoundProducts": []
        }
        """;
    }

    private String getMockGenericResponse() {
        return """
        {
          "products": [],
          "notFoundProducts": []
        }
        """;
    }
}
