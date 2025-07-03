package com.danrley.product_management.common.exception.custom;

public class CategoryNotFoundException extends RuntimeException {

  public CategoryNotFoundException(Long id) {
    super("Categoria não encontrada com o ID: " + id);
  }

  public CategoryNotFoundException(String name) {
    super("Categoria não encontrada com o nome: " + name);
  }

  public CategoryNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}