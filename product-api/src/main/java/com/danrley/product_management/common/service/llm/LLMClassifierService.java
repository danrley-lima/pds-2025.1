package com.danrley.product_management.common.service.llm;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.danrley.product_management.common.enums.RequestCategory;
import com.danrley.product_management.common.service.llm.provider.AIProvider;
import com.danrley.product_management.common.service.llm.provider.AIProviderFactory;

@Service
public class LLMClassifierService {

    private final AIProviderFactory aiProviderFactory;

    public LLMClassifierService(AIProviderFactory aiProviderFactory) {
        this.aiProviderFactory = aiProviderFactory;
    }

    public RequestCategory classifyMessage(String customerMessage) {
        try {
            String prompt = buildClassificationPrompt(customerMessage);
            
            // Usar o provedor de IA através da factory
            AIProvider provider = aiProviderFactory.getProvider();
            Map<String, Object> parameters = getClassificationParameters();
            String response = provider.generateContent(prompt, parameters);
            
            return parseClassificationResponse(response);
        } catch (Exception e) {
            // Em caso de erro, categoria padrão
            return RequestCategory.SEARCH_PRODUCT;
        }
    }

    private String buildClassificationPrompt(String customerMessage) {
        return """
            Classifique a seguinte mensagem do cliente em uma das categorias abaixo.
            Retorne APENAS a categoria, sem explicações ou texto adicional.
            
            Categorias disponíveis:
            - RECIPE: quando o cliente quer fazer uma receita, cozinhar algo, ou menciona ingredientes para preparo
            - SEARCH_PRODUCT: quando o cliente busca por produtos específicos, marcas, ou itens para comprar
            - SEARCH_PROMOTION: quando o cliente busca por ofertas, promoções, descontos, ou produtos em promoção
            - UNKNOWN: quando não se encaixa em nenhuma categoria acima
            
            Exemplos:
            - "Quero fazer um bolo de chocolate" → RECIPE
            - "Preciso de farinha de trigo" → SEARCH_PRODUCT  
            - "Quais produtos estão em promoção?" → SEARCH_PROMOTION
            - "Olá, como vai?" → UNKNOWN
            
            Mensagem do cliente: """ + customerMessage + """
            
            Categoria:""";
    }

    private Map<String, Object> getClassificationParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("temperature", 0.0); 
        parameters.put("maxOutputTokens", 50);  // Resposta curta
        return parameters;
    }

    private RequestCategory parseClassificationResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            return RequestCategory.UNKNOWN;
        }

        String cleanResponse = response.trim().toUpperCase();
        
        // Tenta fazer match exato primeiro
        try {
            return RequestCategory.valueOf(cleanResponse);
        } catch (IllegalArgumentException e) {
            // Se não conseguir match exato, tenta por palavra-chave
            if (cleanResponse.contains("RECIPE")) {
                return RequestCategory.RECIPE;
            } else if (cleanResponse.contains("SEARCH_PRODUCT")) {
                return RequestCategory.SEARCH_PRODUCT;
            } else if (cleanResponse.contains("SEARCH_PROMOTION")) {
                return RequestCategory.SEARCH_PROMOTION;
            } else if (cleanResponse.contains("UNKNOWN")) {
                return RequestCategory.UNKNOWN;
            }
            
            // Fallback por palavras-chave do conteúdo
            String originalMessage = response.toLowerCase();
            if (originalMessage.contains("receita") || originalMessage.contains("cozinhar") || 
                originalMessage.contains("ingredientes") || originalMessage.contains("preparo")) {
                return RequestCategory.RECIPE;
            } else if (originalMessage.contains("promoção") || originalMessage.contains("oferta") || 
                       originalMessage.contains("desconto")) {
                return RequestCategory.SEARCH_PROMOTION;
            } else if (originalMessage.contains("produto") || originalMessage.contains("busca") || 
                       originalMessage.contains("procura")) {
                return RequestCategory.SEARCH_PRODUCT;
            }
            
            return RequestCategory.UNKNOWN;
        }
    }
}
