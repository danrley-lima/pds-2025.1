package com.danrley.product_management.domain.grocery.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.danrley.product_management.core.model.BasePromotionCondition;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroceryPromotionCondition extends BasePromotionCondition<GroceryProduct> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private static final int DEFAULT_DAYS_BEFORE_EXPIRY = 3;

    @Override
    public boolean isEligible(GroceryProduct product) {
        if (product.getExpirationDate() == null) {
            return false;
        }
        
        // Calcula os dias até o vencimento
        LocalDate today = LocalDate.now();
        LocalDate expiryDate = product.getExpirationDate();
        long daysUntilExpiry = ChronoUnit.DAYS.between(today, expiryDate);
        
        return daysUntilExpiry <= DEFAULT_DAYS_BEFORE_EXPIRY;
    }
    
}
