package com.danrley.product_management.dto.recipe;

import java.util.List;

import com.danrley.product_management.dto.product.ProductResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AIRequestPayload {
  @JsonProperty("customer_message")
  private String pedido;

  @JsonProperty("products")
  private List<ProductResponseDTO> produtosDisponiveis;

  public String getPedido() {
    return pedido;
  }

  public void setPedido(String pedido) {
    this.pedido = pedido;
  }

  public List<ProductResponseDTO> getProdutosDisponiveis() {
    return produtosDisponiveis;
  }

  public void setProdutosDisponiveis(List<ProductResponseDTO> produtosDisponiveis) {
    this.produtosDisponiveis = produtosDisponiveis;
  }
}