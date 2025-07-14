package com.danrley.product_management.domain.construction;

import org.springframework.stereotype.Component;

import com.danrley.product_management.core.domain.Domain;
import com.danrley.product_management.core.domain.DomainConfiguration;
import com.danrley.product_management.core.model.BaseCategory;
import com.danrley.product_management.core.model.BaseProduct;

/**
 * Configuração específica para o domínio de material de construção.
 */
@Component
public class ConstructionDomainConfiguration implements DomainConfiguration {

    @Override
    public Domain getDomain() {
        return Domain.CONSTRUCTION;
    }

    @Override
    public BaseCategory[] getDefaultCategories() {
        return new BaseCategory[] {
            new ConstructionCategory(null, "Cimento e Argamassa"),
            new ConstructionCategory(null, "Tijolos e Blocos"),
            new ConstructionCategory(null, "Telhas e Coberturas"),
            new ConstructionCategory(null, "Pisos e Revestimentos"),
            new ConstructionCategory(null, "Ferragens e Parafusos"),
            new ConstructionCategory(null, "Tubos e Conexões"),
            new ConstructionCategory(null, "Tintas e Vernizes"),
            new ConstructionCategory(null, "Ferramentas"),
            new ConstructionCategory(null, "Elétrico e Hidráulico")
        };
    }

    @Override
    public String getProductSearchPromptTemplate() {
        return "Você é um especialista em construção civil que recebe pedidos de materiais de clientes e deve retornar " +
               "apenas um JSON puro e válido, sem explicações, sem texto adicional e sem " +
               "marcação markdown.\n\n" +
               "Aqui está o catálogo de materiais de construção disponíveis:\n" +
               "{products}\n\n" +
               "Com base no catálogo disponível, identifique os materiais que correspondem " +
               "ao que o cliente está buscando para sua obra ou projeto.\n\n" +
               "Considere especificações técnicas, qualidade, marca e aplicação dos materiais. " +
               "Priorize produtos disponíveis, depois produtos em promoção, " +
               "depois produtos profissionais/premium.\n\n" +
               "Se algum material solicitado não estiver disponível no catálogo, " +
               "inclua-o em uma lista separada chamada 'not_found_products'.\n\n" +
               "{json_format}\n\n" +
               "Pedido do cliente: {customer_message}";
    }

    @Override
    public String getRecipePromptTemplate() {
        return "Você é um mestre de obras experiente que vai sugerir etapas de construção e projetos " +
               "com base nos materiais solicitados pelo cliente.\n\n" +
               "Materiais disponíveis:\n{products}\n\n" +
               "Baseado na solicitação do cliente e nos materiais disponíveis, sugira etapas de construção " +
               "ou projetos que utilizem esses materiais, incluindo dicas técnicas e ordem de execução.\n\n" +
               "Para cada material necessário, use o product_id do catálogo. " +
               "Se algum material necessário não estiver disponível no catálogo, " +
               "inclua-o na lista 'missing_materials' com nome e quantidade.\n\n" +
               "Formato da resposta deve ser um JSON válido com os projetos/etapas sugeridos.\n\n" +
               "{json_format}\n\n" +
               "Solicitação do cliente: {customer_message}";
    }

    @Override
    public String getPromotionPromptTemplate() {
        return "Você é um vendedor especializado em materiais de construção que identifica promoções para o cliente.\n\n" +
               "Materiais em promoção:\n{products}\n\n" +
               "Com base na busca do cliente, identifique as melhores ofertas de materiais de construção disponíveis.\n\n" +
               "{json_format}\n\n" +
               "Busca do cliente: {customer_message}";
    }

    @Override
    public String[] getSupportedUnits() {
        return new String[] {"UN", "M2", "M3", "KG", "SC", "CX", "PCT", "ML", "L", "BAR", "ROLO"};
    }

    @Override
    public String getProductResponseJsonFormat() {
        return "Formato esperado:\n" +
               "{\n" +
               "  \"products\": [\n" +
               "    {\"id\": \"string\", \"name\": \"string\", \"brand\": \"string\", \"category_name\": \"string\", " +
               "\"unit_price\": \"string\", \"promotional_price\": \"string\", \"stock_quantity\": \"string\", " +
               "\"specification\": \"string\", \"application\": \"string\"}\n" +
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
               "  \"construction_steps\": [\n" +
               "    {\n" +
               "      \"name\": \"string\",\n" +
               "      \"materials\": [\n" +
               "        {\"product_id\": 123, \"quantity\": \"10 unidades\"},\n" +
               "        {\"product_id\": 456, \"quantity\": \"50kg\"}\n" +
               "      ],\n" +
               "      \"instructions\": \"string\",\n" +
               "      \"estimated_time\": \"string\",\n" +
               "      \"difficulty\": \"string\",\n" +
               "      \"tips\": [\"string\"],\n" +
               "      \"missing_materials\": [\n" +
               "        {\"name\": \"Cimento especial\", \"quantity\": \"5 sacos\"}\n" +
               "      ]\n" +
               "    }\n" +
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
        return product.getDomain() == Domain.CONSTRUCTION;
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
     * Categoria específica para material de construção.
     */
    private static class ConstructionCategory implements BaseCategory {
        private final Long id;
        private final String name;

        public ConstructionCategory(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public Long getId() { return id; }

        @Override
        public String getName() { return name; }

        @Override
        public Domain getDomain() { return Domain.CONSTRUCTION; }

        @Override
        public BaseCategory getParentCategory() { return null; }

        @Override
        public String getDescription() { return "Categoria de " + name; }
    }
}
