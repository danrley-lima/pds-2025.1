package com.danrley.product_management.domain.grocery.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.danrley.product_management.core.model.BasePromotionCondition;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class GroceryPromotionCondition extends BasePromotionCondition<GroceryProduct> {

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

    @Override
    public void applyCondition(List<GroceryProduct> products, double discountPercentage) {
        for (GroceryProduct product : products) {
            if (isEligible(product)) {
                // Aplica a condição de promoção, por exemplo, reduzindo o preço
                double discount = product.getUnitPrice() * discountPercentage;
                product.setUnitPrice(product.getUnitPrice() - discount);

            }
        }
    }

}
