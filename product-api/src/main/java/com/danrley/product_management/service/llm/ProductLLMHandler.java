package com.danrley.product_management.service.llm;

import org.springframework.stereotype.Service;

import com.danrley.product_management.service.llm.provider.AIProviderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductLLMHandler extends AbstractLLMHandler {

    public ProductLLMHandler(AIProviderFactory aiProviderFactory, ObjectMapper objectMapper) {
        super(aiProviderFactory, objectMapper);
    }

    @Override
    protected String buildPrompt(String customerMessage, String productsStr) {
        return buildProductSearchPrompt(customerMessage, productsStr);
    }

    private String buildProductSearchPrompt(String customerMessage, String productsStr) {
        return "Você é um assistente que recebe um pedido de busca de produtos de um cliente e deve retornar " +
               "apenas um JSON puro e válido, sem explicações, sem texto adicional e sem " +
               "marcação markdown.\n\n" +
               "Aqui está a lista de produtos disponíveis no formato: id,name,brand,category_name," +
               "unit_weight unit_type,unit_price,available,on_promotion,promotional_price,stock_quantity,priority;:\n" +
               productsStr + "\n\n" +
               "Com base apenas nesses produtos disponíveis, identifique os produtos que correspondem " +
               "ao que o cliente está buscando.\n\n" +
               "Se o cliente buscar por categoria, retorne todos os produtos dessa categoria disponíveis. " +
               "Se buscar por produto específico, retorne os produtos que correspondem mais proximamente.\n\n" +
               "Priorize produtos disponíveis (available=true), depois produtos em promoção, " +
               "depois produtos com priority=true.\n\n" +
               "Se algum produto solicitado não estiver disponível no banco de dados, " +
               "inclua-o em uma lista separada chamada 'not_found_products'.\n\n" +
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
               "    {\"name\": \"string\", \"quantity\": \"1\"}\n" +
               "  ]\n" +
               "}\n\n";
    }
}
