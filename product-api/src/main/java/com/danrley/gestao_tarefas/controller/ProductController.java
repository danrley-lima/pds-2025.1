package com.danrley.gestao_tarefas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danrley.gestao_tarefas.dto.product.ProductRequestDTO;
import com.danrley.gestao_tarefas.dto.product.ProductResponseDTO;
import com.danrley.gestao_tarefas.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  @GetMapping
  public List<ProductResponseDTO> getAll() {
    return productService.getAll();
  }

  @GetMapping("/{id}")
  public ProductResponseDTO getById(@PathVariable Long id) {
    return productService.getById(id);
  }

  @PostMapping
  public ProductResponseDTO create(@RequestBody ProductRequestDTO dto) {
    return productService.create(dto);
  }

  @PutMapping("/{id}")
  public ProductResponseDTO update(@PathVariable Long id, @RequestBody ProductRequestDTO dto) {
    return productService.update(id, dto);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    productService.delete(id);
  }
}
