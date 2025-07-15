package com.danrley.product_management.core.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "promotion_conditions")
public abstract class BasePromotionCondition<T extends BaseProduct>{ 
    
    protected String conditionDescription;

    public abstract boolean isEligible(T product);

    public void setConditionDescription(String conditionDescription) {
        this.conditionDescription = conditionDescription;
    }

    public String getConditionDescription() {
        return conditionDescription;
    }

}
