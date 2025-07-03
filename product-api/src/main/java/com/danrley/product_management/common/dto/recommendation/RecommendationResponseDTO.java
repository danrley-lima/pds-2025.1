package com.danrley.product_management.common.dto.recommendation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para resposta de recomendações de produtos")
public class RecommendationResponseDTO {
    
    @JsonProperty("products")
    @Schema(description = "Lista de produtos recomendados")
    private List<ProductOutDTO> products;
    
    @JsonProperty("notFoundProducts")
    @Schema(description = "Lista de produtos não encontrados")
    private List<ProductNotFoundDTO> notFoundProducts;

    // Constructors
    public RecommendationResponseDTO() {}

    public RecommendationResponseDTO(List<ProductOutDTO> products, List<ProductNotFoundDTO> notFoundProducts) {
        this.products = products;
        this.notFoundProducts = notFoundProducts;
    }

    // Getters and Setters
    public List<ProductOutDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductOutDTO> products) {
        this.products = products;
    }

    public List<ProductNotFoundDTO> getNotFoundProducts() {
        return notFoundProducts;
    }

    public void setNotFoundProducts(List<ProductNotFoundDTO> notFoundProducts) {
        this.notFoundProducts = notFoundProducts;
    }
}
