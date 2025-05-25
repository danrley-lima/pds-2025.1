package com.danrley.gestao_tarefas.dto.recipe;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para solicitação de receita ao assistente de IA")
public class RecipeRequestDTO {

  @NotBlank(message = "A receita não pode estar em branco")
  @Schema(description = "Nome da receita desejada", example = "Strogonoff de Frango com arroz para 20 pessoas")
  private String receita;

  public String getReceita() {
    return receita;
  }

  public void setReceita(String receita) {
    this.receita = receita;
  }
}