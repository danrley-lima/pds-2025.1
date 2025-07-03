package com.danrley.product_management.common.service.llm.provider;

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
    

}
