package com.danrley.product_management.domain.furniture.repository;

import com.danrley.product_management.domain.furniture.model.FurnitureProduct;
import com.danrley.product_management.common.model.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para produtos de móveis.
 */
@Repository
public interface FurnitureProductRepository extends JpaRepository<FurnitureProduct, Long> {

    // Buscar por categoria
    List<FurnitureProduct> findByCategory(Category category);

    // Buscar por disponibilidade
    List<FurnitureProduct> findByAvailableTrue();

    // Buscar produtos em destaque
    List<FurnitureProduct> findByPriorityTrue();

    // Buscar por faixa de preço
    List<FurnitureProduct> findByUnitPriceBetween(Double minPrice, Double maxPrice);

    // Buscar por material
    List<FurnitureProduct> findByMaterialContainingIgnoreCase(String material);

    // Buscar por cor
    List<FurnitureProduct> findByColorContainingIgnoreCase(String color);

    // Buscar por estilo
    List<FurnitureProduct> findByStyleContainingIgnoreCase(String style);

    // Buscar por nome ou marca
    @Query("SELECT fp FROM FurnitureProduct fp WHERE " +
           "LOWER(fp.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(fp.brand) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<FurnitureProduct> findByNameOrBrandContainingIgnoreCase(@Param("search") String search);

    // Buscar produtos disponíveis por categoria
    List<FurnitureProduct> findByCategoryAndAvailableTrue(Category category);

    // Contar produtos por categoria
    long countByCategory(Category category);

    // Buscar por múltiplos critérios
    @Query("SELECT fp FROM FurnitureProduct fp WHERE " +
           "fp.available = true AND " +
           "(:material IS NULL OR LOWER(fp.material) LIKE LOWER(CONCAT('%', :material, '%'))) AND " +
           "(:color IS NULL OR LOWER(fp.color) LIKE LOWER(CONCAT('%', :color, '%'))) AND " +
           "(:style IS NULL OR LOWER(fp.style) LIKE LOWER(CONCAT('%', :style, '%'))) AND " +
           "(:minPrice IS NULL OR fp.unitPrice >= :minPrice) AND " +
           "(:maxPrice IS NULL OR fp.unitPrice <= :maxPrice)")
    List<FurnitureProduct> findByMultipleCriteria(
        @Param("material") String material,
        @Param("color") String color, 
        @Param("style") String style,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice
    );
}
