package com.danrley.product_management.core.model;

import java.time.LocalDate;

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

@Entity
@Data
@AllArgsConstructor
@Table(name = "promotions")
public abstract class BasePromotion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected double promotionalPrice;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected String description;

    public BasePromotion(){
    }
    
    public BasePromotion(double promotionalPrice, LocalDate startDate, LocalDate endDate, String description) {
        this.promotionalPrice = promotionalPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    // @OneToOne
    // @JoinColumn(name = "furniture_product_id")
    // @JsonBackReference
    // private FurnitureProduct furnitureProduct;
    
    // @OneToOne
    // @JoinColumn(name = "construction_product_id")
    // @JsonBackReference
    // private ConstructionProduct constructionProduct;
}
