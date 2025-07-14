package com.danrley.product_management.domain.construction.repository;

import com.danrley.product_management.domain.construction.model.ConstructionProduct;
import com.danrley.product_management.common.model.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para produtos de construção.
 */
@Repository
public interface ConstructionProductRepository extends JpaRepository<ConstructionProduct, Long> {

    // Buscar por categoria
    List<ConstructionProduct> findByCategory(Category category);

    // Buscar por disponibilidade
    List<ConstructionProduct> findByAvailableTrue();

    // Buscar produtos em destaque
    List<ConstructionProduct> findByPriorityTrue();

    // Buscar por faixa de preço
    List<ConstructionProduct> findByUnitPriceBetween(Double minPrice, Double maxPrice);

    // Buscar por categoria de construção específica
    List<ConstructionProduct> findByConstructionCategoryContainingIgnoreCase(String constructionCategory);

    // Buscar por aplicação
    List<ConstructionProduct> findByApplicationContainingIgnoreCase(String application);

    // Buscar por grau/qualidade
    List<ConstructionProduct> findByGradeContainingIgnoreCase(String grade);

    // Buscar por especificações
    List<ConstructionProduct> findBySpecificationsContainingIgnoreCase(String specifications);

    // Buscar por nome ou marca
    @Query("SELECT cp FROM ConstructionProduct cp WHERE " +
           "LOWER(cp.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(cp.brand) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<ConstructionProduct> findByNameOrBrandContainingIgnoreCase(@Param("search") String search);

    // Buscar produtos disponíveis por categoria
    List<ConstructionProduct> findByCategoryAndAvailableTrue(Category category);

    // Contar produtos por categoria
    long countByCategory(Category category);

    // Buscar por múltiplos critérios
    @Query("SELECT cp FROM ConstructionProduct cp WHERE " +
           "cp.available = true AND " +
           "(:constructionCategory IS NULL OR LOWER(cp.constructionCategory) LIKE LOWER(CONCAT('%', :constructionCategory, '%'))) AND " +
           "(:application IS NULL OR LOWER(cp.application) LIKE LOWER(CONCAT('%', :application, '%'))) AND " +
           "(:grade IS NULL OR LOWER(cp.grade) LIKE LOWER(CONCAT('%', :grade, '%'))) AND " +
           "(:minPrice IS NULL OR cp.unitPrice >= :minPrice) AND " +
           "(:maxPrice IS NULL OR cp.unitPrice <= :maxPrice)")
    List<ConstructionProduct> findByMultipleCriteria(
        @Param("constructionCategory") String constructionCategory,
        @Param("application") String application, 
        @Param("grade") String grade,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice
    );
}
