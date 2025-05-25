package com.danrley.gestao_tarefas.dto.recipe;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Produto não encontrado no estoque")
public class MissingProductDTO {

  @Schema(description = "Nome do produto não encontrado")
  @JsonProperty("nome")
  private String nome;

  @Schema(description = "Quantidade necessária do produto")
  @JsonProperty("quantidade")
  private String quantidade;

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getQuantidade() {
    return quantidade;
  }

  public void setQuantidade(String quantidade) {
    this.quantidade = quantidade;
  }
}