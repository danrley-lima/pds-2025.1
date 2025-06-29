package com.danrley.product_management.service.llm;

import org.springframework.stereotype.Service;

import com.danrley.product_management.service.llm.provider.AIProviderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PromotionLLMHandler extends AbstractLLMHandler {

    public PromotionLLMHandler(AIProviderFactory aiProviderFactory, ObjectMapper objectMapper) {
        super(aiProviderFactory, objectMapper);
    }

    @Override
    protected String buildPrompt(String customerMessage, String productsStr) {
        return buildPromotionSearchPrompt(customerMessage, productsStr);
    }

    private String buildPromotionSearchPrompt(String customerMessage, String productsStr) {
        return "Você é um assistente que recebe um pedido de busca de promoções de um cliente e deve retornar " +
               "apenas um JSON puro e válido, sem explicações, sem texto adicional e sem " +
               "marcação markdown.\n\n" +
               "Aqui está a lista de produtos disponíveis no formato: id,name,brand,category_name," +
               "unit_weight unit_type,unit_price,available,on_promotion,promotional_price,stock_quantity,priority;:\n" +
               productsStr + "\n\n" +
               "Com base apenas nesses produtos disponíveis, identifique os produtos EM PROMOÇÃO " +
               "(on_promotion=true) que correspondem ao que o cliente está buscando.\n\n" +
               "IMPORTANTE: Retorne APENAS produtos que estejam em promoção (on_promotion=true). " +
               "Se o cliente buscar por categoria, retorne todos os produtos em promoção dessa categoria. " +
               "Se buscar por produto específico, retorne os produtos em promoção que correspondem.\n\n" +
               "Priorize produtos disponíveis (available=true) e com melhor desconto " +
               "(diferença entre unit_price e promotional_price).\n\n" +
               "Se não houver produtos em promoção que correspondam ao pedido, " +
               "inclua na lista 'not_found_products' com uma mensagem explicativa.\n\n" +
               getExpectedJsonFormat() +
               "Pedido do cliente: " + customerMessage;
    }

    private String getExpectedJsonFormat() {
        return "Formato esperado:\n" +
               "{\n" +
               "  \"products\": [\n" +
               "    {\"id\": \"string\", \"name\": \"string\", \"brand\": \"string\", \"category_name\": \"string\", " +
               "\"unit_price\": \"string\", \"promotional_price\": \"string\", \"stock_quantity\": \"string\"}\n" +
               "  ],\n" +
               "  \"not_found_products\": [\n" +
               "    {\"name\": \"string\", \"quantity\": \"Nenhuma promoção encontrada\"}\n" +
               "  ]\n" +
               "}\n\n";
    }
}
