package com.danrley.product_management.service.llm;

import org.springframework.stereotype.Service;

import com.danrley.product_management.service.llm.provider.AIProviderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RecipeLLMHandler extends AbstractLLMHandler {

    public RecipeLLMHandler(AIProviderFactory aiProviderFactory, ObjectMapper objectMapper) {
        super(aiProviderFactory, objectMapper);
    }

    @Override
    protected String buildPrompt(String customerMessage, String productsStr) {
        return buildRecipePrompt(customerMessage, productsStr);
    }

    private String buildRecipePrompt(String customerMessage, String productsStr) {
        return "Você é um assistente que recebe um pedido de receita de um cliente e deve retornar " +
               "apenas um JSON puro e válido, sem explicações, sem texto adicional e sem " +
               "marcação markdown.\n\n" +
               "Aqui está a lista de produtos disponíveis no formato: id,name,brand,category_name," +
               "unit_weight unit_type,unit_price,available,on_promotion,promotional_price,stock_quantity,priority;:\n" +
               productsStr + "\n\n" +
               "Com base apenas nesses produtos disponíveis, extraia os produtos e quantidades necessários " +
               "para a receita solicitada abaixo.\n\n" +
               "Sempre converta as quantidades para valores numéricos de medida padrão (gramas, mililitros, " +
               "litros, quilos, etc.), " +
               "e SEMPRE informe a unidade junto ao valor, por exemplo: '200g', '2L', '500ml', '1kg'. Nunca " +
               "retorne apenas o número.\n\n" +
               "Se algum produto necessário para a receita não estiver disponível no banco de dados, " +
               "inclua-o em uma lista separada chamada 'not_found_products', informando apenas " +
               "o nome e a quantidade desejada, também com unidade.\n\n" +
               "Se houver mais de um produto disponível que atenda ao mesmo propósito (por exemplo, diferentes " +
               "marcas de arroz ou produtos substitutos), escolha apenas UM produto para cada necessidade. " +
               "Priorize primeiro os produtos em promoção, depois aqueles marcados com priority==true, " +
               "e por ultimo os produtos normais. Caso haja empate, escolha o de maior quantidade. Não " +
               "repita produtos equivalentes na lista final.\n\n" +
               "Se a receita não especificar detalhes adicionais, como quantidade de pessoas ou porções, " +
               "considere que a receita é para 1 pessoa.\n\n" +
               getExpectedJsonFormat() +
               "Pedido do cliente: " + customerMessage;
    }

    private String getExpectedJsonFormat() {
        return "Formato esperado:\n" +
               "{\n" +
               "  \"products\": [\n" +
               "    {\"id\": \"string\", \"name\": \"string\", \"brand\": \"string\", \"category_name\": \"string\", " +
               "\"unit_price\": \"string\", \"promotional_price\": \"string\", \"stock_quantity\": \"string\", " +
               "\"required_quantity\": \"string\"}\n" +
               "  ],\n" +
               "  \"not_found_products\": [\n" +
               "    {\"name\": \"string\", \"quantity\": \"string\"}\n" +
               "  ]\n" +
               "}\n\n";
    }
}
