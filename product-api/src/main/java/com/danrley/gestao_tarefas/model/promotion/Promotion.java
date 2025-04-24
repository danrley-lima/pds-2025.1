package com.danrley.gestao_tarefas.model.promotion;

import java.time.LocalDate;

import com.danrley.gestao_tarefas.model.product.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promotions")
public class Promotion {
    public Promotion(double promotionalPrice, LocalDate startDate, LocalDate endDate, Product product) {
        this.promotionalPrice = promotionalPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.product = product;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double promotionalPrice;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
}
