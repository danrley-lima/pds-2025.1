package com.danrley.product_management.common.model.promotion;

import java.time.LocalDate;

import com.danrley.product_management.domain.grocery.model.GroceryProduct;
import com.danrley.product_management.domain.furniture.model.FurnitureProduct;
import com.danrley.product_management.domain.construction.model.ConstructionProduct;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promotions")
public class Promotion {
    
    public Promotion(double promotionalPrice, String description, LocalDate startDate, LocalDate endDate, GroceryProduct groceryProduct) {
        this.promotionalPrice = promotionalPrice;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.groceryProduct = groceryProduct;
        this.furnitureProduct = null;
        this.constructionProduct = null;
    }
    
    public Promotion(double promotionalPrice, String description, LocalDate startDate, LocalDate endDate, FurnitureProduct furnitureProduct) {
        this.promotionalPrice = promotionalPrice;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.furnitureProduct = furnitureProduct;
        this.groceryProduct = null;
        this.constructionProduct = null;
    }
    
    public Promotion(double promotionalPrice, String description, LocalDate startDate, LocalDate endDate, ConstructionProduct constructionProduct) {
        this.promotionalPrice = promotionalPrice;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.constructionProduct = constructionProduct;
        this.groceryProduct = null;
        this.furnitureProduct = null;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double promotionalPrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    
    @OneToOne
    @JoinColumn(name = "grocery_product_id")
    @JsonBackReference
    private GroceryProduct groceryProduct;
    
    @OneToOne
    @JoinColumn(name = "furniture_product_id")
    @JsonBackReference
    private FurnitureProduct furnitureProduct;
    
    @OneToOne
    @JoinColumn(name = "construction_product_id")
    @JsonBackReference
    private ConstructionProduct constructionProduct;
    
}
