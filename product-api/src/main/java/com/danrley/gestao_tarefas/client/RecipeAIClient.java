package com.danrley.gestao_tarefas.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.danrley.gestao_tarefas.dto.product.ProductResponseDTO;
import com.danrley.gestao_tarefas.dto.recipe.AIRequestPayload;
import com.danrley.gestao_tarefas.dto.recipe.RecipeResponseDTO;
import com.danrley.gestao_tarefas.exception.custom.RecipeAIServiceException;
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

      HttpEntity<String> requestEntity = new HttpEntity<>(
          objectMapper.writeValueAsString(payload));

      ResponseEntity<String> response = restTemplate.exchange(
          apiUrl + "/receita",
          HttpMethod.POST,
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