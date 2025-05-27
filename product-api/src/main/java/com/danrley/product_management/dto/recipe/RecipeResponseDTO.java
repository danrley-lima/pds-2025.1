package com.danrley.product_management.dto.recipe;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta com sugestão de produtos para a receita")
public class RecipeResponseDTO {

  @Schema(description = "Lista de produtos necessários para a receita")
  @JsonProperty("products")
  private List<ProductSuggestionDTO> produtos;

  @Schema(description = "Lista de produtos que não foram encontrados no estoque")
  @JsonProperty("notFoundProducts")
  private List<MissingProductDTO> produtosNaoEncontrados;

  public List<ProductSuggestionDTO> getProdutos() {
    return produtos;
  }

  public void setProdutos(List<ProductSuggestionDTO> produtos) {
    this.produtos = produtos;
  }

  public List<MissingProductDTO> getProdutosNaoEncontrados() {
    return produtosNaoEncontrados;
  }

  public void setProdutosNaoEncontrados(List<MissingProductDTO> produtosNaoEncontrados) {
    this.produtosNaoEncontrados = produtosNaoEncontrados;
  }
}