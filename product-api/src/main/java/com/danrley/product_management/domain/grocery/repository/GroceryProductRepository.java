package com.danrley.product_management.domain.grocery.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.danrley.product_management.domain.grocery.model.GroceryProduct;
import com.danrley.product_management.common.model.category.Category;

/**
 * Repositório específico para produtos do domínio grocery.
 */
public interface GroceryProductRepository extends JpaRepository<GroceryProduct, Long> {
    
    Optional<GroceryProduct> findByName(String name);
    
    List<GroceryProduct> findByCategory(Category category);
    
    List<GroceryProduct> findByBrand(String brand);
    
    List<GroceryProduct> findByAvailable(boolean available);
    
    List<GroceryProduct> findByPriority(boolean priority);
    
    @Query("SELECT gp FROM GroceryProduct gp WHERE gp.available = true")
    List<GroceryProduct> findAvailableProducts();
    
    @Query("SELECT gp FROM GroceryProduct gp WHERE gp.priority = true")
    List<GroceryProduct> findPriorityProducts();
    
    @Query("SELECT gp FROM GroceryProduct gp JOIN gp.promotion pr WHERE pr.startDate <= :today AND pr.endDate >= :today")
    List<GroceryProduct> findProductsWithActivePromotions(@Param("today") LocalDate today);
    
    @Query("SELECT gp FROM GroceryProduct gp WHERE gp.stockQuantity > 0")
    List<GroceryProduct> findInStock();
    
    @Query("SELECT gp FROM GroceryProduct gp WHERE gp.unitPrice BETWEEN :minPrice AND :maxPrice")
    List<GroceryProduct> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    long countByAvailable(boolean available);
    
    long countByCategory(Category category);
}
