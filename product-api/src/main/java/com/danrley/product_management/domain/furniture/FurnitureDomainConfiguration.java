package com.danrley.product_management.domain.furniture;

import org.springframework.stereotype.Component;

import com.danrley.product_management.framework.domain.Domain;
import com.danrley.product_management.framework.domain.DomainConfiguration;
import com.danrley.product_management.framework.model.BaseCategory;
import com.danrley.product_management.framework.model.BaseProduct;

/**
 * Configuração específica para o domínio de móveis e decoração.
 */
@Component
public class FurnitureDomainConfiguration implements DomainConfiguration {

    @Override
    public Domain getDomain() {
        return Domain.FURNITURE;
    }

    @Override
    public BaseCategory[] getDefaultCategories() {
        return new BaseCategory[] {
            new FurnitureCategory(null, "Sofás e Poltronas"),
            new FurnitureCategory(null, "Mesas e Cadeiras"),
            new FurnitureCategory(null, "Camas e Colchões"),
            new FurnitureCategory(null, "Guarda-roupas"),
            new FurnitureCategory(null, "Estantes e Prateleiras"),
            new FurnitureCategory(null, "Decoração"),
            new FurnitureCategory(null, "Iluminação"),
            new FurnitureCategory(null, "Eletrodomésticos")
        };
    }

    @Override
    public String getProductSearchPromptTemplate() {
        return "Você é um consultor especialista em móveis e decoração que recebe pedidos de clientes e deve retornar " +
               "apenas um JSON puro e válido, sem explicações, sem texto adicional e sem " +
               "marcação markdown.\n\n" +
               "Aqui está o catálogo de móveis disponíveis:\n" +
               "{products}\n\n" +
               "Com base no catálogo disponível, identifique os móveis que correspondem " +
               "ao que o cliente está buscando.\n\n" +
               "Considere estilo, tamanho, material, cor e funcionalidade na busca. " +
               "Priorize produtos disponíveis, depois produtos em promoção, " +
               "depois produtos em destaque.\n\n" +
               "Se algum móvel solicitado não estiver disponível no catálogo, " +
               "inclua-o em uma lista separada chamada 'not_found_products'.\n\n" +
               "{json_format}\n\n" +
               "Pedido do cliente: {customer_message}";
    }

    @Override
    public String getRecipePromptTemplate() {
        return "Você é um designer de interiores especialista que vai sugerir ambientes e projetos de decoração " +
               "com base nos móveis solicitados pelo cliente.\n\n" +
               "Móveis disponíveis:\n{products}\n\n" +
               "Baseado na solicitação do cliente e nos móveis disponíveis, sugira projetos de decoração " +
               "que utilizem esses itens, incluindo dicas de combinação, cores e layout.\n\n" +
               "Formato da resposta deve ser um JSON válido com os projetos sugeridos.\n\n" +
               "{json_format}\n\n" +
               "Solicitação do cliente: {customer_message}";
    }

    @Override
    public String getPromotionPromptTemplate() {
        return "Você é um consultor de vendas especializado em móveis que identifica promoções para o cliente.\n\n" +
               "Móveis em promoção:\n{products}\n\n" +
               "Com base na busca do cliente, identifique as melhores ofertas de móveis disponíveis.\n\n" +
               "{json_format}\n\n" +
               "Busca do cliente: {customer_message}";
    }

    @Override
    public String[] getSupportedUnits() {
        return new String[] {"UN", "PAR", "CONJUNTO", "M2", "CM"};
    }

    @Override
    public String getProductResponseJsonFormat() {
        return "Formato esperado:\n" +
               "{\n" +
               "  \"products\": [\n" +
               "    {\"id\": \"string\", \"name\": \"string\", \"brand\": \"string\", \"category_name\": \"string\", " +
               "\"unit_price\": \"string\", \"promotional_price\": \"string\", \"stock_quantity\": \"string\", " +
               "\"dimensions\": \"string\", \"material\": \"string\", \"color\": \"string\"}\n" +
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
               "  \"projects\": [\n" +
               "    {\"name\": \"string\", \"items\": [\"string\"], \"description\": \"string\", " +
               "\"style\": \"string\", \"color_palette\": [\"string\"], \"tips\": [\"string\"]}\n" +
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
        return product.getDomain() == Domain.FURNITURE;
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
     * Categoria específica para móveis.
     */
    private static class FurnitureCategory implements BaseCategory {
        private final Long id;
        private final String name;

        public FurnitureCategory(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public Long getId() { return id; }

        @Override
        public String getName() { return name; }

        @Override
        public Domain getDomain() { return Domain.FURNITURE; }

        @Override
        public BaseCategory getParentCategory() { return null; }

        @Override
        public String getDescription() { return "Categoria de " + name; }
    }
}
