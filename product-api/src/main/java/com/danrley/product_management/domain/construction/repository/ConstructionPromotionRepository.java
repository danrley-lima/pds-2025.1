package com.danrley.product_management.domain.construction.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.danrley.product_management.domain.construction.model.ConstructionPromotion;

public interface ConstructionPromotionRepository extends JpaRepository<ConstructionPromotion, Long> {

}
