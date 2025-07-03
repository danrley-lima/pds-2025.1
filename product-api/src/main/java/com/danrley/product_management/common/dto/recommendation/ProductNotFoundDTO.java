package com.danrley.product_management.common.dto.recommendation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductNotFoundDTO {
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("quantity")
    private String quantity;

    // Constructors
    public ProductNotFoundDTO() {}

    public ProductNotFoundDTO(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
