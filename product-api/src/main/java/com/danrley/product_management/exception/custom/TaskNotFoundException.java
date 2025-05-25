package com.danrley.product_management.exception.custom;

public class TaskNotFoundException extends RuntimeException {

  public TaskNotFoundException(String message) {
    super(message);
  }
}
