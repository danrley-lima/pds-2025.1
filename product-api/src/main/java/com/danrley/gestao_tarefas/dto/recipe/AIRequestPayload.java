package com.danrley.gestao_tarefas.dto.recipe;

import java.util.List;

import com.danrley.gestao_tarefas.dto.product.ProductResponseDTO;

public class AIRequestPayload {
    private String receita;
    private List<ProductResponseDTO> produtosDisponiveis;

    public String getReceita() {
        return receita;
    }

    public void setReceita(String receita) {
        this.receita = receita;
    }

    public List<ProductResponseDTO> getProdutosDisponiveis() {
        return produtosDisponiveis;
    }

    public void setProdutosDisponiveis(List<ProductResponseDTO> produtosDisponiveis) {
        this.produtosDisponiveis = produtosDisponiveis;
    }
}