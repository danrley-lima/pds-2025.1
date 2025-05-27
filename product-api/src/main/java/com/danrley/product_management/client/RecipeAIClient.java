package com.danrley.product_management.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.danrley.product_management.dto.product.ProductResponseDTO;
import com.danrley.product_management.dto.recipe.AIRequestPayload;
import com.danrley.product_management.dto.recipe.RecipeResponseDTO;
import com.danrley.product_management.exception.custom.RecipeAIServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RecipeAIClient {

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;
  private final String apiUrl;

  public RecipeAIClient(
      RestTemplate restTemplate,
      ObjectMapper objectMapper,
      @Value("${ai.recipe.api.url}") String apiUrl) {
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;
    this.apiUrl = apiUrl;
  }

  public RecipeResponseDTO getRecipeSuggestions(String recipe, List<ProductResponseDTO> availableProducts) {
    try {
      AIRequestPayload payload = new AIRequestPayload();
      payload.setPedido(recipe);
      payload.setProdutosDisponiveis(availableProducts);

      HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

      // Criar a requisição com o corpo JSON e os headers
      String jsonBody = objectMapper.writeValueAsString(payload);
      HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
      
      ResponseEntity<String> response = restTemplate.postForEntity(
          apiUrl + "/recommendations",
          requestEntity,
          String.class);

      return objectMapper.readValue(response.getBody(), RecipeResponseDTO.class);

    } catch (JsonProcessingException e) {
      throw new RecipeAIServiceException("Erro ao processar JSON: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RecipeAIServiceException("Erro na comunicação com API de receitas: " + e.getMessage(), e);
    }
  }
}