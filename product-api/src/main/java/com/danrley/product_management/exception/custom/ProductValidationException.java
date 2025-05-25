package com.danrley.product_management.exception.custom;

import java.util.Collections;
import java.util.Map;

/**
 * Exceção lançada quando ocorrem erros de validação nos dados de um produto.
 * Permite armazenar múltiplos erros de validação em um mapa.
 */
public class ProductValidationException extends RuntimeException {

  private final Map<String, String> validationErrors;

  public ProductValidationException(String message) {
    super(message);
    this.validationErrors = Collections.emptyMap();
  }

  public ProductValidationException(String message, Throwable cause) {
    super(message, cause);
    this.validationErrors = Collections.emptyMap();
  }

  /**
   * Construtor para múltiplos erros de validação.
   *
   * @param message          Mensagem geral do erro
   * @param validationErrors Mapa de erros onde a chave é o nome do campo e o
   *                         valor é a mensagem de erro
   */
  public ProductValidationException(String message, Map<String, String> validationErrors) {
    super(message);
    this.validationErrors = validationErrors != null ? Collections.unmodifiableMap(validationErrors)
        : Collections.emptyMap();
  }

  /**
   * Retorna os erros de validação específicos por campo.
   *
   * @return Mapa imutável com os erros de validação
   */
  public Map<String, String> getValidationErrors() {
    return validationErrors;
  }

  /**
   * Verifica se existem erros de validação específicos.
   *
   * @return true se houver erros de validação específicos
   */
  public boolean hasValidationErrors() {
    return !validationErrors.isEmpty();
  }
}