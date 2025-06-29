package com.danrley.product_management.service.llm.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;

import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoogleGeminiProvider implements AIProvider {

    private final String apiKey;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp:generateContent";
    private static final String PROVIDER_NAME = "Google Gemini";

    public GoogleGeminiProvider(@Value("${ai.provider.gemini.api.key:${google.gemini.api.key:}}") String apiKey, 
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
                "API Key do Gemini não configurada. Configure ai.provider.gemini.api.key ou google.gemini.api.key");
        }

        try {
            // Construir o payload da requisição
            Map<String, Object> requestBody = buildRequestBody(prompt, parameters);
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            Mono<String> response = webClient.post()
                    .uri(GEMINI_API_URL + "?key=" + apiKey)
                    .header("Content-Type", "application/json")
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
                "Erro na chamada para a API do Gemini", e);
        }
    }

    @Override
    public Map<String, Object> getDefaultParameters() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("temperature", 0.0);
        defaults.put("maxOutputTokens", 2000);
        defaults.put("topP", 1.0);
        defaults.put("topK", 1);
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
        // Limites do Google Gemini (podem variar conforme o plano)
        return new ProviderLimits(
            8192,  // maxTokensPerRequest
            60,    // requestsPerMinute  
            1500   // requestsPerDay
        );
    }

    private Map<String, Object> buildRequestBody(String prompt, Map<String, Object> parameters) {
        Map<String, Object> requestBody = new HashMap<>();
        
        // Conteúdo da mensagem
        Map<String, Object> content = new HashMap<>();
        Map<String, String> part = new HashMap<>();
        part.put("text", prompt);
        content.put("parts", List.of(part));
        requestBody.put("contents", List.of(content));
        
        // Configurações do modelo
        Map<String, Object> generationConfig = new HashMap<>(getDefaultParameters());
        if (parameters != null) {
            generationConfig.putAll(parameters);
        }
        requestBody.put("generationConfig", generationConfig);
        
        // Configurações de segurança (opcionais)
        requestBody.put("safetySettings", buildSafetySettings());
        
        return requestBody;
    }

    private List<Map<String, Object>> buildSafetySettings() {
        // Configurações básicas de segurança
        return List.of(
            Map.of("category", "HARM_CATEGORY_HARASSMENT", "threshold", "BLOCK_MEDIUM_AND_ABOVE"),
            Map.of("category", "HARM_CATEGORY_HATE_SPEECH", "threshold", "BLOCK_MEDIUM_AND_ABOVE"),
            Map.of("category", "HARM_CATEGORY_SEXUALLY_EXPLICIT", "threshold", "BLOCK_MEDIUM_AND_ABOVE"),
            Map.of("category", "HARM_CATEGORY_DANGEROUS_CONTENT", "threshold", "BLOCK_MEDIUM_AND_ABOVE")
        );
    }

    private String extractTextFromResponse(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode candidates = jsonNode.get("candidates");
            
            if (candidates != null && candidates.isArray() && candidates.size() > 0) {
                JsonNode content = candidates.get(0).get("content");
                if (content != null) {
                    JsonNode parts = content.get("parts");
                    if (parts != null && parts.isArray() && parts.size() > 0) {
                        JsonNode text = parts.get(0).get("text");
                        if (text != null) {
                            return text.asText().trim();
                        }
                    }
                }
            }
            
            // Se não conseguiu extrair, retorna resposta vazia
            return "";
            
        } catch (Exception e) {
            throw new AIProviderException(PROVIDER_NAME, "RESPONSE_PARSING_ERROR", 
                "Erro ao extrair texto da resposta do Gemini", e);
        }
    }
}
