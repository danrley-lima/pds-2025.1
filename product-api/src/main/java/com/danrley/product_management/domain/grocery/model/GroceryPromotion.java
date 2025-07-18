package com.danrley.product_management.domain.grocery.model;

import java.time.LocalDate;

import com.danrley.product_management.core.model.BasePromotion;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class GroceryPromotion extends BasePromotion {
    @OneToOne
    @JoinColumn(name = "grocery_product_id")
    @JsonBackReference
    private GroceryProduct groceryProduct;

    public GroceryPromotion() {
        super();
    }

    public GroceryPromotion(double promotionalPrice, LocalDate startDate, LocalDate endDate, String description, GroceryProduct groceryProduct) {
        super(promotionalPrice, startDate, endDate, description);
        this.groceryProduct = groceryProduct;
    }
}
