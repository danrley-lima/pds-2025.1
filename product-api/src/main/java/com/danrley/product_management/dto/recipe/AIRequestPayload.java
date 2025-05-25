package com.danrley.product_management.dto.recipe;

import java.util.List;

import com.danrley.product_management.dto.product.ProductResponseDTO;

public class AIRequestPayload {
  private String pedido;
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