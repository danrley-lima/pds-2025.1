package com.danrley.gestao_tarefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danrley.gestao_tarefas.model.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
