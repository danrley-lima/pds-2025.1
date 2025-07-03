package com.danrley.product_management.common.exception.custom;

import java.util.List;

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

  public ProductNotFoundException(List<Long> ids) {
    super("Os seguintes produtos não foram encontrados: " + ids);
  }
}