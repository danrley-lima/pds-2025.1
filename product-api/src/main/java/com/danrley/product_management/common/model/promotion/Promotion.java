package com.danrley.product_management.common.model.promotion;

import java.time.LocalDate;

import com.danrley.product_management.core.model.BaseProduct;
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

    public Promotion(double promotionalPrice, LocalDate startDate, LocalDate endDate, String description) {
        this.promotionalPrice = promotionalPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected double promotionalPrice;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected String description;
    
    @OneToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    protected BaseProduct baseProduct;

    // @OneToOne
    // @JoinColumn(name = "furniture_product_id")
    // @JsonBackReference
    // private FurnitureProduct furnitureProduct;
    
    // @OneToOne
    // @JoinColumn(name = "construction_product_id")
    // @JsonBackReference
    // private ConstructionProduct constructionProduct;


    
}
