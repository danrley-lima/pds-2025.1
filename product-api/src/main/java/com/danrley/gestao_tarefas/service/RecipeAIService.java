package com.danrley.gestao_tarefas.service;

import org.springframework.stereotype.Service;

import com.danrley.gestao_tarefas.client.RecipeAIClient;
import com.danrley.gestao_tarefas.dto.recipe.RecipeRequestDTO;
import com.danrley.gestao_tarefas.dto.recipe.RecipeResponseDTO;
import com.danrley.gestao_tarefas.exception.custom.RecipeAIServiceException;

@Service
public class RecipeAIService {

  private final ProductService productService;
  private final RecipeAIClient recipeAIClient;

  public RecipeAIService(ProductService productService, RecipeAIClient recipeAIClient) {
    this.productService = productService;
    this.recipeAIClient = recipeAIClient;
  }

  /**
   * Obtém sugestão de produtos para uma receita
   * 
   * @param recipeRequest a solicitação com o nome da receita
   * @return a resposta com os produtos sugeridos
   */
  public RecipeResponseDTO getSuggestedProducts(RecipeRequestDTO recipeRequest) {
    try {
      var availableProducts = productService.getAll();

      // Uso de chamada com padrão adapter
      return recipeAIClient.getRecipeSuggestions(
          recipeRequest.getReceita(),
          availableProducts);

    } catch (Exception e) {
      throw new RecipeAIServiceException("Erro ao processar solicitação de receita: " + e.getMessage(), e);
    }
  }
}