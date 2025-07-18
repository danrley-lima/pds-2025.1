package com.danrley.product_management.core.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "promotion_conditions")
public abstract class BasePromotionCondition<T extends BaseProduct>{ 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    protected String conditionDescription;

    public abstract boolean isEligible(T product);

    public void setConditionDescription(String conditionDescription) {
        this.conditionDescription = conditionDescription;
    }

    public String getConditionDescription() {
        return conditionDescription;
    }

}
