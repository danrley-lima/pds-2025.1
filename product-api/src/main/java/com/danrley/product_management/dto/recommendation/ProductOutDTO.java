package com.danrley.product_management.dto.recommendation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.danrley.product_management.domain.grocery.model.GroceryProduct;
import com.danrley.product_management.domain.furniture.model.FurnitureProduct;
import com.danrley.product_management.domain.construction.model.ConstructionProduct;

public class ProductOutDTO {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("brand")
    private String brand;
    
    @JsonProperty("categoryName")
    @JsonAlias({"category_name"}) // Aceita ambos os formatos
    private String categoryName;
    
    @JsonProperty("unitPrice")
    @JsonAlias({"unit_price"}) // Aceita ambos os formatos
    private String unitPrice;
    
    @JsonProperty("promotionalPrice")
    @JsonAlias({"promotional_price"}) // Aceita ambos os formatos
    private String promotionalPrice;
    
    @JsonProperty("stockQuantity")
    @JsonAlias({"stock_quantity"}) // Aceita ambos os formatos
    private String stockQuantity;
    
    @JsonProperty("requiredQuantity")
    @JsonAlias({"required_quantity"}) // Aceita ambos os formatos
    private String requiredQuantity;

    // Constructors
    public ProductOutDTO() {}

    public ProductOutDTO(String id, String name, String brand, String categoryName, 
                        String unitPrice, String promotionalPrice, String stockQuantity, String requiredQuantity) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.categoryName = categoryName;
        this.unitPrice = unitPrice;
        this.promotionalPrice = promotionalPrice;
        this.stockQuantity = stockQuantity;
        this.requiredQuantity = requiredQuantity;
    }

    /**
     * Factory method to create ProductOutDTO from GroceryProduct
     */
    public static ProductOutDTO fromGroceryProduct(GroceryProduct product) {
        return new ProductOutDTO(
            product.getId().toString(),
            product.getName(),
            product.getBrand(),
            product.getCategory() != null ? product.getCategory().getName() : "N/A",
            product.getUnitPrice() != null ? product.getUnitPrice().toString() : "0.0",
            product.getPromotion() != null 
                ? String.valueOf(product.getPromotion().getPromotionalPrice()) : null,
            product.getStockQuantity() != null ? product.getStockQuantity().toString() : "0",
            "1" // default required quantity
        );
    }

    /**
     * Factory method to create ProductOutDTO from FurnitureProduct
     */
    public static ProductOutDTO fromFurnitureProduct(FurnitureProduct product) {
        return new ProductOutDTO(
            product.getId().toString(),
            product.getName(),
            product.getBrand(),
            product.getCategory() != null ? product.getCategory().getName() : "Móvel",
            product.getUnitPrice() != null ? product.getUnitPrice().toString() : "0.0",
            null, // Furniture products don't have direct promotion field yet
            product.getStockQuantity() != null ? product.getStockQuantity().toString() : "0",
            "1" // default required quantity
        );
    }

    /**
     * Factory method to create ProductOutDTO from ConstructionProduct
     */
    public static ProductOutDTO fromConstructionProduct(ConstructionProduct product) {
        return new ProductOutDTO(
            product.getId().toString(),
            product.getName(),
            product.getBrand(),
            product.getCategory() != null ? product.getCategory().getName() : "Material",
            product.getUnitPrice() != null ? product.getUnitPrice().toString() : "0.0",
            null, // Construction products don't have direct promotion field yet
            product.getStockQuantity() != null ? product.getStockQuantity().toString() : "0",
            "1" // default required quantity
        );
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getPromotionalPrice() {
        return promotionalPrice;
    }

    public void setPromotionalPrice(String promotionalPrice) {
        this.promotionalPrice = promotionalPrice;
    }

    public String getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(String stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(String requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }
}
