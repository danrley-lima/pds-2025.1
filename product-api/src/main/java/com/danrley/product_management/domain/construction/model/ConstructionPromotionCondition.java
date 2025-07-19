package com.danrley.product_management.domain.construction.model;

import java.util.List;

import com.danrley.product_management.core.model.BasePromotionCondition;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class ConstructionPromotionCondition extends BasePromotionCondition<ConstructionProduct> {
    public ConstructionPromotionCondition() {
        super();
    }

    public ConstructionPromotionCondition(String description) {
        super(description);
    }


    @Override
    public boolean isEligible(ConstructionProduct product) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isEligible'");
    }

    @Override
    public void applyCondition(List<ConstructionProduct> products, double discountPercentage) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'applyCondition'");
    }
    
}
