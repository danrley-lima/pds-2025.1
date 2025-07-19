package com.danrley.product_management.domain.construction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danrley.product_management.domain.construction.model.ConstructionPromotionCondition;

public interface ConstructionPromotionConditionRepository extends JpaRepository<ConstructionPromotionCondition, Long> {

    // Additional methods specific to ConstructionPromotionCondition can be added here if needed
    
}
