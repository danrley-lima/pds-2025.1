package com.danrley.product_management.common.model.category;

import com.danrley.product_management.core.domain.Domain;
import com.danrley.product_management.core.model.BaseCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name = "categories")
public class Category implements BaseCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "domain")
  private Domain domain = Domain.GROCERY; // Default para compatibilidade

  public Category() {
  }

  public Category(String name) {
    this.name = name;
    this.domain = Domain.GROCERY;
  }
  
  public Category(String name, Domain domain) {
    this.name = name;
    this.domain = domain;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Domain getDomain() {
    return domain;
  }
  
  public void setDomain(Domain domain) {
    this.domain = domain;
  }

  @Override
  public BaseCategory getParentCategory() {
    return null; // Atualmente não suportamos hierarquia de categorias
  }

  @Override
  public String getDescription() {
    return "Categoria de " + name;
  }
}
