package com.danrley.product_management.dto.recipe;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalhes do produto sugerido para a receita")
public class ProductSuggestionDTO {

  @Schema(description = "ID do produto")
  private String id;

  @Schema(description = "Nome do produto")
  @JsonProperty("name")
  private String nome;

  @Schema(description = "Marca do produto")
  @JsonProperty("brand")
  private String marca;

  @Schema(description = "Preço unitário do produto")
  @JsonProperty("unitPrice")
  private String preco;

  @Schema(description = "Quantidade total necessária para a receita")
  @JsonProperty("stockQuantity")
  private String quantidadeTotal;

  @Schema(description = "Descrição das embalagens necessárias")
  @JsonProperty("requiredQuantity")
  private String embalagensNecessarias;

  @Schema(description = "Categoria do produto")
  @JsonProperty("categoryName")
  private String categoria;

  @Schema(description = "Preço promocional do produto, se aplicável")
  @JsonProperty("promotionalPrice")
  private String precoPromocional;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getMarca() {
    return marca;
  }

  public void setMarca(String marca) {
    this.marca = marca;
  }

  public String getPreco() {
    return preco;
  }

  public void setPreco(String preco) {
    this.preco = preco;
  }

  public String getQuantidadeTotal() {
    return quantidadeTotal;
  }

  public void setQuantidadeTotal(String quantidadeTotal) {
    this.quantidadeTotal = quantidadeTotal;
  }

  public String getEmbalagensNecessarias() {
    return embalagensNecessarias;
  }

  public void setEmbalagensNecessarias(String embalagensNecessarias) {
    this.embalagensNecessarias = embalagensNecessarias;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public String getPrecoPromocional() {
    return precoPromocional;
  }
  
  public void setPrecoPromocional(String precoPromocional) {
    this.precoPromocional = precoPromocional;
  }
}