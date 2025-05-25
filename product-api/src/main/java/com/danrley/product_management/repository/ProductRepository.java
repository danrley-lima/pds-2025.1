package com.danrley.product_management.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.danrley.product_management.model.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
  Optional<Product> findByName(String name);

  @Query("SELECT p FROM Product p JOIN p.promotion pr WHERE pr.startDate <= :today AND pr.endDate >= :today")
  List<Product> findProductsWithActivePromotions(@Param("today") LocalDate today);
}
