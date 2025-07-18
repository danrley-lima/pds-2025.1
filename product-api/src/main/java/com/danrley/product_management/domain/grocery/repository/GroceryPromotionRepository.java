package com.danrley.product_management.domain.grocery.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danrley.product_management.domain.grocery.model.GroceryPromotion;

public interface GroceryPromotionRepository extends JpaRepository<GroceryPromotion, Long> {
    List<GroceryPromotion> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate hoje1, LocalDate hoje2);
}
