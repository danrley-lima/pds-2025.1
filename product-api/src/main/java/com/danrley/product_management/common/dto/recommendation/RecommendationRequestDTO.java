package com.danrley.product_management.common.dto.recommendation;

import java.util.List;

import com.danrley.product_management.common.dto.product.ProductResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para requisição de recomendações de produtos usando LLM")
public class RecommendationRequestDTO {
    
    @NotBlank(message = "A mensagem do cliente é obrigatória")
    @JsonProperty("customer_message")
    @Schema(description = "Mensagem do cliente solicitando recomendações", example = "Quero fazer um bolo de chocolate")
    private String customerMessage;
    
    @NotNull(message = "A lista de produtos é obrigatória")
    @JsonProperty("products")
    @Schema(description = "Lista de produtos disponíveis para recomendação")
    private List<ProductResponseDTO> products;

    // Getters and Setters
    public String getCustomerMessage() {
        return customerMessage;
    }

    public void setCustomerMessage(String customerMessage) {
        this.customerMessage = customerMessage;
    }

    public List<ProductResponseDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponseDTO> products) {
        this.products = products;
    }
}
