package com.danrley.gestao_tarefas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danrley.gestao_tarefas.dto.recipe.RecipeRequestDTO;
import com.danrley.gestao_tarefas.dto.recipe.RecipeResponseDTO;
import com.danrley.gestao_tarefas.service.RecipeAIService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recipes")
@Tag(name = "Receitas", description = "Operações relacionadas a receitas e assistente de compras")
public class RecipeController {

  @Autowired
  private RecipeAIService recipeAIService;

  @PostMapping("/suggest")
  @Operation(summary = "Sugerir produtos para receita", description = "Recebe o nome de uma receita e retorna os produtos necessários")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produtos sugeridos com sucesso", content = @Content(schema = @Schema(implementation = RecipeResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Solicitação inválida"),
      @ApiResponse(responseCode = "500", description = "Erro ao processar a requisição")
  })
  public ResponseEntity<RecipeResponseDTO> suggestProducts(@Valid @RequestBody RecipeRequestDTO request) {
    RecipeResponseDTO response = recipeAIService.getSuggestedProducts(request);
    return ResponseEntity.ok(response);
  }
}