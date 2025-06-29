package com.danrley.product_management.service.llm.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;

import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementação do provedor OpenAI GPT
 * Ativado quando ai.provider.openai.enabled=true
 */
@Service
@ConditionalOnProperty(name = "ai.provider.openai.enabled", havingValue = "true")
public class OpenAIProvider implements AIProvider {

    private final String apiKey;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String PROVIDER_NAME = "OpenAI GPT";

    public OpenAIProvider(@Value("${ai.provider.openai.api.key:}") String apiKey, 
                         WebClient.Builder webClientBuilder, 
                         ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    public String generateContent(String prompt, Map<String, Object> parameters) {
        if (!isConfigured()) {
            throw new AIProviderException(PROVIDER_NAME, "API_KEY_NOT_CONFIGURED", 
                "API Key do OpenAI não configurada. Configure ai.provider.openai.api.key");
        }

        try {
            Map<String, Object> requestBody = buildRequestBody(prompt, parameters);
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            Mono<String> response = webClient.post()
                    .uri(OPENAI_API_URL)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(jsonBody)
                    .retrieve()
                    .bodyToMono(String.class);

            String rawResponse = response.block();
            return extractTextFromResponse(rawResponse);

        } catch (JsonProcessingException e) {
            throw new AIProviderException(PROVIDER_NAME, "JSON_PROCESSING_ERROR", 
                "Erro ao processar JSON para requisição", e);
        } catch (Exception e) {
            throw new AIProviderException(PROVIDER_NAME, "API_CALL_ERROR", 
                "Erro na chamada para a API do OpenAI", e);
        }
    }

    @Override
    public Map<String, Object> getDefaultParameters() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("temperature", 0.0);
        defaults.put("max_tokens", 2000);
        defaults.put("model", "gpt-3.5-turbo"); // ou gpt-4
        return defaults;
    }

    @Override
    public boolean isConfigured() {
        return apiKey != null && !apiKey.trim().isEmpty();
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public ProviderLimits getLimits() {
        // Limites do OpenAI (variam conforme o plano)
        return new ProviderLimits(
            4096,  // maxTokensPerRequest (para gpt-3.5-turbo)
            3500,  // requestsPerMinute (tier 1)
            200000 // requestsPerDay (tier 1)
        );
    }

    private Map<String, Object> buildRequestBody(String prompt, Map<String, Object> parameters) {
        Map<String, Object> requestBody = new HashMap<>();
        
        // Modelo e configurações
        Map<String, Object> config = new HashMap<>(getDefaultParameters());
        if (parameters != null) {
            config.putAll(parameters);
        }
        
        requestBody.put("model", config.get("model"));
        requestBody.put("temperature", config.get("temperature"));
        requestBody.put("max_tokens", config.get("max_tokens"));
        
        // Mensagens no formato do OpenAI
        List<Map<String, String>> messages = List.of(
            Map.of("role", "user", "content", prompt)
        );
        requestBody.put("messages", messages);
        
        return requestBody;
    }

    private String extractTextFromResponse(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode choices = jsonNode.get("choices");
            
            if (choices != null && choices.isArray() && choices.size() > 0) {
                JsonNode message = choices.get(0).get("message");
                if (message != null) {
                    JsonNode content = message.get("content");
                    if (content != null) {
                        return content.asText().trim();
                    }
                }
            }
            
            return "";
            
        } catch (Exception e) {
            throw new AIProviderException(PROVIDER_NAME, "RESPONSE_PARSING_ERROR", 
                "Erro ao extrair texto da resposta do OpenAI", e);
        }
    }
}
