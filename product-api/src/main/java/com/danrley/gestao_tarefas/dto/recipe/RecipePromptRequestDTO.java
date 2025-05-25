package com.danrley.gestao_tarefas.dto.recipe;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para solicitação de receita ao assistente de IA")
public class RecipePromptRequestDTO {

  @NotBlank(message = "O prompt não pode estar em branco")
  @Schema(description = "Prompt descrevendo a receita desejada", example = "Quero fazer um bolo de chocolate para 10 pessoas")
  private String prompt;

  public String getPrompt() {
    return prompt;
  }

  public void setPrompt(String prompt) {
    this.prompt = prompt;
  }
}