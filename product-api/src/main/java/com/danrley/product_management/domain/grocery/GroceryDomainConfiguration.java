package com.danrley.product_management.domain.grocery;

import org.springframework.stereotype.Component;

import com.danrley.product_management.core.domain.Domain;
import com.danrley.product_management.core.domain.DomainConfiguration;
import com.danrley.product_management.core.model.BaseCategory;
import com.danrley.product_management.core.model.BaseProduct;

/**
 * Configuração específica para o domínio de supermercado/alimentação.
 */
@Component
public class GroceryDomainConfiguration implements DomainConfiguration {

    @Override
    public Domain getDomain() {
        return Domain.GROCERY;
    }

    @Override
    public BaseCategory[] getDefaultCategories() {
        // Retorna as categorias padrão para supermercado
        return new BaseCategory[] {
            new GroceryCategory(null, "Frutas e Vegetais"),
            new GroceryCategory(null, "Carnes e Peixes"),
            new GroceryCategory(null, "Laticínios"),
            new GroceryCategory(null, "Padaria"),
            new GroceryCategory(null, "Bebidas"),
            new GroceryCategory(null, "Congelados"),
            new GroceryCategory(null, "Higiene e Limpeza"),
            new GroceryCategory(null, "Mercearia")
        };
    }

    @Override
    public String getProductSearchPromptTemplate() {
        return "Você é um assistente que recebe um pedido de busca de produtos de supermercado de um cliente e deve retornar " +
               "apenas um JSON puro e válido, sem explicações, sem texto adicional e sem " +
               "marcação markdown.\n\n" +
               "Aqui está a lista de produtos disponíveis no formato: id,name,brand,category_name," +
               "unit_weight unit_type,unit_price,available,on_promotion,promotional_price,stock_quantity,priority;:\n" +
               "{products}\n\n" +
               "Com base apenas nesses produtos disponíveis, identifique os produtos que correspondem " +
               "ao que o cliente está buscando.\n\n" +
               "Se o cliente buscar por categoria, retorne todos os produtos dessa categoria disponíveis. " +
               "Se buscar por produto específico, retorne os produtos que correspondem mais proximamente.\n\n" +
               "Priorize produtos disponíveis (available=true), depois produtos em promoção, " +
               "depois produtos com priority=true.\n\n" +
               "Se algum produto solicitado não estiver disponível no banco de dados, " +
               "inclua-o em uma lista separada chamada 'not_found_products'.\n\n" +
               "{json_format}\n\n" +
               "Pedido do cliente: {customer_message}";
    }

    @Override
    public String getRecipePromptTemplate() {
        return "Você é um chef especialista que vai sugerir receitas com base nos produtos solicitados pelo cliente.\n\n" +
               "Produtos disponíveis:\n{products}\n\n" +
               "Baseado na solicitação do cliente e nos produtos disponíveis, sugira receitas que utilizem esses ingredientes.\n\n" +
               "IMPORTANTE: Retorne APENAS o nome da receita e a lista de ingredientes necessários. " +
               "NÃO inclua instruções de preparo nem tempo de preparo.\n\n" +
               "Para cada ingrediente, inclua a quantidade necessária entre parênteses quando relevante.\n" +
               "Exemplo: [\"Carne Moída (500g)\", \"Tomate (2 unidades)\", \"Cebola (1 unidade)\"]\n\n" +
               "Formato da resposta deve ser um JSON válido com as receitas sugeridas.\n\n" +
               "{json_format}\n\n" +
               "Solicitação do cliente: {customer_message}";
    }

    @Override
    public String getPromotionPromptTemplate() {
        return "Você é um assistente de vendas que identifica produtos em promoção para o cliente.\n\n" +
               "Produtos em promoção:\n{products}\n\n" +
               "Com base na busca do cliente, identifique as melhores promoções disponíveis.\n\n" +
               "{json_format}\n\n" +
               "Busca do cliente: {customer_message}";
    }

    @Override
    public String[] getSupportedUnits() {
        return new String[] {"G", "KG", "ML", "L", "UN", "PCT"};
    }

    @Override
    public String getProductResponseJsonFormat() {
        return "Formato esperado:\n" +
               "{\n" +
               "  \"products\": [\n" +
               "    {\"id\": \"string\", \"name\": \"string\", \"brand\": \"string\", \"category_name\": \"string\", " +
               "\"unit_price\": \"string\", \"promotional_price\": \"string\", \"stock_quantity\": \"string\"}\n" +
               "  ],\n" +
               "  \"not_found_products\": [\n" +
               "    {\"name\": \"string\", \"quantity\": \"1\"}\n" +
               "  ]\n" +
               "}";
    }

    @Override
    public String getRecipeResponseJsonFormat() {
        return "Formato esperado:\n" +
               "{\n" +
               "  \"recipes\": [\n" +
               "    {\"name\": \"string\", \"ingredients\": [\"string\"]}\n" +
               "  ]\n" +
               "}";
    }

    @Override
    public boolean isValidProduct(BaseProduct product) {
        if (product == null || product.getName() == null || product.getName().trim().isEmpty()) {
            return false;
        }
        if (product.getUnitPrice() == null || product.getUnitPrice() < 0) {
            return false;
        }
        return product.getDomain() == Domain.GROCERY;
    }

    @Override
    public String formatProductForPrompt(BaseProduct product) {
        return String.format("%d,%s,%s,%s,%.2f,%b,%b,%d,%b",
            product.getId(),
            product.getName(),
            product.getBrand() != null ? product.getBrand() : "",
            product.getCategory() != null ? product.getCategory().getName() : "",
            product.getUnitPrice(),
            product.isAvailable(),
            false, // promotion placeholder
            product.getStockQuantity(),
            product.isPriority()
        );
    }

    /**
     * Categoria específica para supermercado.
     */
    private static class GroceryCategory implements BaseCategory {
        private final Long id;
        private final String name;

        public GroceryCategory(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public Long getId() { return id; }

        @Override
        public String getName() { return name; }

        @Override
        public Domain getDomain() { return Domain.GROCERY; }

        @Override
        public BaseCategory getParentCategory() { return null; }

        @Override
        public String getDescription() { return "Categoria de " + name; }
    }
}
