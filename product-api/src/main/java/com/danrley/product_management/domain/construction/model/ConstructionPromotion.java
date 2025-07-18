package com.danrley.product_management.domain.construction.model;

import java.time.LocalDate;

import com.danrley.product_management.core.model.BasePromotion;
import com.danrley.product_management.domain.grocery.model.GroceryProduct;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class ConstructionPromotion extends BasePromotion {
    @OneToOne
    @JoinColumn(name = "construction_product_id")
    @JsonBackReference
    private ConstructionProduct constructionProduct;

    public ConstructionPromotion() {
        super();
    }

    public ConstructionPromotion(double promotionalPrice, LocalDate startDate, LocalDate endDate, String description, ConstructionProduct constructionProduct) {
        super(promotionalPrice, startDate, endDate, description);
        this.constructionProduct = constructionProduct;
    }
}
