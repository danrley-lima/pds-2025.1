package com.danrley.product_management.domain.construction.model;

import com.danrley.product_management.framework.domain.Domain;
import com.danrley.product_management.framework.model.BaseProduct;
import com.danrley.product_management.model.category.Category;
import com.danrley.product_management.model.product.UnitType;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Produto específico para o domínio de construção.
 * Entidade JPA para produtos do domínio construction.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "construction_products")
public class ConstructionProduct implements BaseProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String brand;

    @Column(name = "unit_weight")
    private Double unitWeight;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_type")
    private UnitType unitType;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "unit_price")
    private Double unitPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonManagedReference
    private Category category;

    private boolean available = true;

    private boolean priority = false;

    // Campos específicos do domínio construction
    private String specifications;
    @Column(name = "construction_category")
    private String constructionCategory;
    private String application;
    private String grade;

    // Construtores específicos para construction
    public ConstructionProduct(String name, String brand, Double unitWeight, UnitType unitType, 
                              Integer stockQuantity, Double unitPrice, Category category, 
                              boolean available, boolean priority, String specifications, 
                              String constructionCategory, String application, String grade) {
        this.name = name;
        this.brand = brand;
        this.unitWeight = unitWeight;
        this.unitType = unitType;
        this.stockQuantity = stockQuantity;
        this.unitPrice = unitPrice;
        this.category = category;
        this.available = available;
        this.priority = priority;
        this.specifications = specifications;
        this.constructionCategory = constructionCategory;
        this.application = application;
        this.grade = grade;
    }

    // Implementação dos métodos da interface BaseProduct
    @Override
    public Domain getDomain() { return Domain.CONSTRUCTION; }
    
    @Override
    public boolean isAvailable() { return available; }
    
    @Override
    public boolean isPriority() { return priority; }

    @Override
    public String serializeForPrompt() {
        return String.format("%d,%s,%s,%s,%.2f,%b,%b,%d,%b,especificacoes=%s,categoria=%s,aplicacao=%s,grau=%s",
            id, name, brand != null ? brand : "", 
            category != null ? category.getName() : "",
            unitPrice, available, false, // promotion placeholder
            stockQuantity, priority,
            specifications != null ? specifications : "",
            constructionCategory != null ? constructionCategory : "",
            application != null ? application : "",
            grade != null ? grade : ""
        );
    }
    
    // Métodos de conveniência para compatibilidade
    public boolean getAvailable() { return available; }
    public boolean getPriority() { return priority; }
}
