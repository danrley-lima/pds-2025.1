package com.danrley.product_management.service.llm.provider;

/**
 * Exception específica para erros de provedores de IA
 */
public class AIProviderException extends RuntimeException {
    
    private final String providerName;
    private final String errorCode;
    
    public AIProviderException(String providerName, String message) {
        super(message);
        this.providerName = providerName;
        this.errorCode = null;
    }
    
    public AIProviderException(String providerName, String message, Throwable cause) {
        super(message, cause);
        this.providerName = providerName;
        this.errorCode = null;
    }
    
    public AIProviderException(String providerName, String errorCode, String message) {
        super(message);
        this.providerName = providerName;
        this.errorCode = errorCode;
    }
    
    public AIProviderException(String providerName, String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.providerName = providerName;
        this.errorCode = errorCode;
    }
    
    public String getProviderName() {
        return providerName;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    @Override
    public String getMessage() {
        String baseMessage = super.getMessage();
        if (errorCode != null) {
            return String.format("[%s - %s] %s", providerName, errorCode, baseMessage);
        } else {
            return String.format("[%s] %s", providerName, baseMessage);
        }
    }
}
