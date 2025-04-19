package com.danrley.gestao_tarefas.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danrley.gestao_tarefas.dto.product.ProductRequestDTO;
import com.danrley.gestao_tarefas.dto.product.ProductResponseDTO;
import com.danrley.gestao_tarefas.model.category.Category;
import com.danrley.gestao_tarefas.model.product.Product;
import com.danrley.gestao_tarefas.repository.CategoryRepository;
import com.danrley.gestao_tarefas.repository.ProductRepository;

@Service
public class ProductService {

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private ProductRepository productRepository;

  public List<ProductResponseDTO> getAll() {
    return productRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
  }

  public ProductResponseDTO getById(Long id) {
    return productRepository.findById(id).map(this::toResponse).orElse(null);
  }

  public ProductResponseDTO create(ProductRequestDTO dto) {
    Product product = new Product();
    mapRequestToProduct(dto, product);
    return toResponse(productRepository.save(product));
  }

  public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
    Product product = productRepository.findById(id).orElseThrow();
    mapRequestToProduct(dto, product);
    return toResponse(productRepository.save(product));
  }

  public void delete(Long id) {
    productRepository.deleteById(id);
  }

  private void mapRequestToProduct(ProductRequestDTO dto, Product product) {
    if (dto.name != null)
      product.setName(dto.name);
    if (dto.brand != null)
      product.setBrand(dto.brand);
    if (dto.unitWeight != null)
      product.setUnitWeight(dto.unitWeight);
    if (dto.unitType != null)
      product.setUnitType(dto.unitType);
    if (dto.stockQuantity != null)
      product.setStockQuantity(dto.stockQuantity);
    if (dto.unitPrice != null)
      product.setUnitPrice(dto.unitPrice);
    if (dto.categoryId != null) {
      Category category = categoryRepository.findById(dto.categoryId)
          .orElseThrow(() -> new RuntimeException("Category not found"));
      product.setCategory(category);
    }
  }

  private ProductResponseDTO toResponse(Product product) {
    ProductResponseDTO dto = new ProductResponseDTO();
    dto.id = product.getId();
    dto.name = product.getName();
    dto.brand = product.getBrand();
    dto.unitWeight = product.getUnitWeight();
    dto.unitType = product.getUnitType();
    dto.stockQuantity = product.getStockQuantity();
    dto.unitPrice = product.getUnitPrice();
    dto.available = product.isAvailable();
    dto.categoryName = product.getCategory() != null ? product.getCategory().getName() : null;
    return dto;
  }

  public List<ProductResponseDTO> getByIds(List<Long> ids) {
    return productRepository.findAllById(ids)
        .stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
}
}
