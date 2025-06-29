package com.danrley.product_management.service.llm.provider;

import java.util.Map;

/**
 * Interface para provedores de IA/LLM
 * Permite abstrair diferentes provedores como Gemini, OpenAI, Claude, etc.
 */
public interface AIProvider {
    
    /**
     * Gera conteúdo baseado no prompt fornecido
     * 
     * @param prompt O prompt/texto para o modelo de IA
     * @param parameters Parâmetros específicos do modelo (temperatura, max tokens, etc.)
     * @return Resposta gerada pela IA
     * @throws AIProviderException Em caso de erro na chamada
     */
    String generateContent(String prompt, Map<String, Object> parameters);
    
    /**
     * Gera conteúdo com parâmetros padrão
     * 
     * @param prompt O prompt/texto para o modelo de IA
     * @return Resposta gerada pela IA
     * @throws AIProviderException Em caso de erro na chamada
     */
    default String generateContent(String prompt) {
        return generateContent(prompt, getDefaultParameters());
    }
    
    /**
     * Retorna os parâmetros padrão para este provedor
     * 
     * @return Map com parâmetros padrão
     */
    Map<String, Object> getDefaultParameters();
    
    /**
     * Verifica se o provedor está configurado corretamente
     * 
     * @return true se configurado, false caso contrário
     */
    boolean isConfigured();
    
    /**
     * Retorna o nome do provedor
     * 
     * @return Nome do provedor (ex: "Google Gemini", "OpenAI GPT", etc.)
     */
    String getProviderName();
    
    /**
     * Retorna informações sobre limites do provedor
     * 
     * @return Informações sobre limites (tokens, requests per minute, etc.)
     */
    ProviderLimits getLimits();
    
    /**
     * Classe para informações sobre limites do provedor
     */
    class ProviderLimits {
        private final int maxTokensPerRequest;
        private final int requestsPerMinute;
        private final int requestsPerDay;
        
        public ProviderLimits(int maxTokensPerRequest, int requestsPerMinute, int requestsPerDay) {
            this.maxTokensPerRequest = maxTokensPerRequest;
            this.requestsPerMinute = requestsPerMinute;
            this.requestsPerDay = requestsPerDay;
        }
        
        public int getMaxTokensPerRequest() { return maxTokensPerRequest; }
        public int getRequestsPerMinute() { return requestsPerMinute; }
        public int getRequestsPerDay() { return requestsPerDay; }
    }
}
