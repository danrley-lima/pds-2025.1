package com.danrley.product_management.exception.custom;

public class RecipeAIServiceException extends RuntimeException {

  public RecipeAIServiceException(String message) {
    super(message);
  }

  public RecipeAIServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}