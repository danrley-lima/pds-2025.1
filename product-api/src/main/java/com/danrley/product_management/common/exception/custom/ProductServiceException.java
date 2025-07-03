package com.danrley.product_management.common.exception.custom;

/**
 * Exceção lançada quando ocorrem erros gerais na camada de serviço de produtos.
 * Usada para encapsular exceções técnicas ou não mapeadas especificamente.
 */
public class ProductServiceException extends RuntimeException {

  public ProductServiceException(String message) {
    super(message);
  }

  public ProductServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}