package com.danrley.product_management.domain.furniture.model;

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
 * Produto específico para o domínio de móveis.
 * Entidade JPA para produtos do domínio furniture.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "furniture_products")
public class FurnitureProduct implements BaseProduct {

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

    // Campos específicos do domínio furniture
    private String dimensions;
    private String material;
    private String color;
    private String style;

    // Construtores específicos para furniture
    public FurnitureProduct(String name, String brand, Double unitWeight, UnitType unitType, 
                           Integer stockQuantity, Double unitPrice, Category category, 
                           boolean available, boolean priority, String dimensions, 
                           String material, String color, String style) {
        this.name = name;
        this.brand = brand;
        this.unitWeight = unitWeight;
        this.unitType = unitType;
        this.stockQuantity = stockQuantity;
        this.unitPrice = unitPrice;
        this.category = category;
        this.available = available;
        this.priority = priority;
        this.dimensions = dimensions;
        this.material = material;
        this.color = color;
        this.style = style;
    }

    // Implementação dos métodos da interface BaseProduct
    @Override
    public Domain getDomain() { return Domain.FURNITURE; }
    
    @Override
    public boolean isAvailable() { return available; }
    
    @Override
    public boolean isPriority() { return priority; }

    @Override
    public String serializeForPrompt() {
        return String.format("%d,%s,%s,%s,%.2f,%b,%b,%d,%b,dimensoes=%s,material=%s,cor=%s,estilo=%s",
            id, name, brand != null ? brand : "", 
            category != null ? category.getName() : "",
            unitPrice, available, false, // promotion placeholder
            stockQuantity, priority,
            dimensions != null ? dimensions : "",
            material != null ? material : "",
            color != null ? color : "",
            style != null ? style : ""
        );
    }
    
    // Métodos de conveniência para compatibilidade
    public boolean getAvailable() { return available; }
    public boolean getPriority() { return priority; }

}
