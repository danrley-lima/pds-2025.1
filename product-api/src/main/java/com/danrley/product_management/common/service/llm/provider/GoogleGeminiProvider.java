package com.danrley.product_management.common.service.llm.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GoogleGeminiProvider implements AIProvider {

  private final String apiKey;
  private final WebClient webClient;
  private final ObjectMapper objectMapper;
  private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-lite:generateContent";
  // private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp:generateContent";
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
          "Chave da API do Gemini não configurada");
    }

    try {
      Map<String, Object> body = buildRequestBody(prompt, parameters);

      String response = webClient.post()
          .uri(GEMINI_API_URL + "?key=" + apiKey)
          .header("Content-Type", "application/json")
          .bodyValue(body)
          .retrieve()
          .bodyToMono(String.class)
          .block();

      return extractTextFromResponse(response);

    } catch (Exception e) {
      throw new AIProviderException(PROVIDER_NAME, "API_ERROR", "Erro na API do Gemini", e);
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

  private Map<String, Object> buildRequestBody(String prompt, Map<String, Object> parameters) {
    Map<String, Object> body = new HashMap<>();

    body.put("contents", List.of(
        Map.of("parts", List.of(Map.of("text", prompt)))));

    // Map<String, Object> config = new HashMap<>(getDefaultParameters());
    // if (parameters != null) {
    // config.putAll(parameters);
    // }
    // body.put("generationConfig", config);

    return body;
  }

  private String extractTextFromResponse(String response) {
    try {
      JsonNode json = objectMapper.readTree(response);
      JsonNode candidates = json.get("candidates");

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

      return "";

    } catch (Exception e) {
      throw new AIProviderException(PROVIDER_NAME, "RESPONSE_ERROR",
          "Erro ao extrair texto da resposta", e);
    }
  }
}
