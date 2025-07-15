package com.danrley.product_management.domain.grocery.repository;

import com.danrley.product_management.domain.grocery.model.GroceryPromotionCondition;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroceryPromotionConditionRepository extends JpaRepository<GroceryPromotionCondition, Long>{

}
