package com.danrley.product_management.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danrley.product_management.common.dto.ErrorResponseDto;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class NotFoundController {

  @RequestMapping("/**")
  public ResponseEntity<ErrorResponseDto> handleNotFound(HttpServletRequest request) {
    String requestUri = request.getRequestURI();
    String method = request.getMethod();

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponseDto(
            HttpStatus.NOT_FOUND.value(),
            "Endpoint Not Found",
            String.format("No endpoint found for %s %s. Please check the URL and try again.", method, requestUri)));
  }
}
