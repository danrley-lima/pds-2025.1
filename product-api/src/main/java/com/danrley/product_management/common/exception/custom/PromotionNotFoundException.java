package com.danrley.product_management.common.exception.custom;

public class PromotionNotFoundException extends RuntimeException {
    
    public PromotionNotFoundException(Long id) {
        super("Promoção não encontrada com o ID: " + id);
    }
    
    public PromotionNotFoundException(String message) {
        super(message);
    }
    
    public PromotionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}