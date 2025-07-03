package com.danrley.product_management.common.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danrley.product_management.common.model.promotion.Promotion;



public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate hoje1, LocalDate hoje2);

}
