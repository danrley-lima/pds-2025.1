package com.danrley.product_management.exception.custom;

public class ProductNotFoundException extends RuntimeException {

  public ProductNotFoundException(Long id) {
    super("Produto não encontrado com o ID: " + id);
  }

  public ProductNotFoundException(String name) {
    super("Produto não encontrado com o nome: " + name);
  }

  public ProductNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}