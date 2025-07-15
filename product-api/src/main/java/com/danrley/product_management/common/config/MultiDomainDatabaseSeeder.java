package com.danrley.product_management.common.config;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.danrley.product_management.common.model.category.Category;
import com.danrley.product_management.common.model.product.UnitType;
import com.danrley.product_management.common.repository.CategoryRepository;
import com.danrley.product_management.core.domain.Domain;
import com.danrley.product_management.domain.construction.model.ConstructionProduct;
import com.danrley.product_management.domain.construction.repository.ConstructionProductRepository;
import com.danrley.product_management.domain.furniture.model.FurnitureProduct;
import com.danrley.product_management.domain.furniture.repository.FurnitureProductRepository;
import com.danrley.product_management.domain.grocery.model.GroceryProduct;
import com.danrley.product_management.domain.grocery.repository.GroceryProductRepository;

import lombok.RequiredArgsConstructor;

/**
 * Seeder para popular o banco de dados.
 * Cria produtos de exemplo para todos os domínios.
 */
@Component
@Profile({ "dev", "test" })
@RequiredArgsConstructor
@Order(5)
public class MultiDomainDatabaseSeeder implements CommandLineRunner {

  @Value("${app.database-seeder-enabled:false}")
  private boolean isSeederEnabled;

  private final CategoryRepository categoryRepository;
  private final GroceryProductRepository groceryProductRepository;
  private final FurnitureProductRepository furnitureProductRepository;
  private final ConstructionProductRepository constructionProductRepository;

  @Override
  public void run(String... args) throws Exception {
    if (!isSeederEnabled) {
      System.out.println("🔵 Database seeder desabilitado.");
      return;
    }

    System.out.println("🚀 Iniciando seed do banco de dados...");

    seedGroceryDomain();
    seedFurnitureDomain();
    seedConstructionDomain();

    System.out.println("✅ Seed concluído!");
  }

  /**
   * Seed do domínio SUPERMERCADO
   */
  private void seedGroceryDomain() {
    System.out.println("🛒 Seeding domínio SUPERMERCADO...");

    if (categoryRepository.countByDomain(Domain.GROCERY) == 0) {
      List<Category> groceryCategories = Arrays.asList(
          new Category("Carnes", Domain.GROCERY),
          new Category("Hortifruti", Domain.GROCERY),
          new Category("Bebidas", Domain.GROCERY),
          new Category("Laticínios", Domain.GROCERY),
          new Category("Grãos e Cereais", Domain.GROCERY),
          new Category("Limpeza", Domain.GROCERY));

      categoryRepository.saveAll(groceryCategories);
      System.out.println("✅ Categorias de supermercado inseridas!");
    }

    if (groceryProductRepository.count() == 0) {
      Category carnes = categoryRepository.findByNameAndDomain("Carnes", Domain.GROCERY).orElseThrow();
      Category hortifruti = categoryRepository.findByNameAndDomain("Hortifruti", Domain.GROCERY).orElseThrow();
      Category bebidas = categoryRepository.findByNameAndDomain("Bebidas", Domain.GROCERY).orElseThrow();
      Category laticinios = categoryRepository.findByNameAndDomain("Laticínios", Domain.GROCERY).orElseThrow();
      Category graos = categoryRepository.findByNameAndDomain("Grãos e Cereais", Domain.GROCERY).orElseThrow();
      Category limpeza = categoryRepository.findByNameAndDomain("Limpeza", Domain.GROCERY).orElseThrow();

      List<GroceryProduct> groceryProducts = Arrays.asList(
          // CARNES (5 produtos)
          new GroceryProduct("Frango em Cubos", "Sadia", 500.0, UnitType.G, 50, 18.0, carnes, true, false),
          new GroceryProduct("Carne Moída", "Friboi", 1000.0, UnitType.G, 100, 22.0, carnes, true, false),
          new GroceryProduct("Peito de Frango", "Sadia", 1000.0, UnitType.G, 80, 15.5, carnes, true, false),
          new GroceryProduct("Linguiça Toscana", "Perdigão", 500.0, UnitType.G, 40, 12.9, carnes, true, false),
          new GroceryProduct("Ovos Brancos", "Korin", 60.0, UnitType.UN, 200, 0.85, carnes, true, false),

          // HORTIFRUTI (5 produtos)
          new GroceryProduct("Cebola", "Natural", 100.0, UnitType.G, 200, 3.5, hortifruti, true, false),
          new GroceryProduct("Tomate", "Agrícola", 200.0, UnitType.G, 150, 4.0, hortifruti, true, false),
          new GroceryProduct("Batata Inglesa", "Natural", 1000.0, UnitType.G, 180, 5.9, hortifruti, true, false),
          new GroceryProduct("Banana Prata", "Natural", 1000.0, UnitType.G, 100, 6.8, hortifruti, true, false),
          new GroceryProduct("Maçã Fuji", "Nacional", 1000.0, UnitType.G, 90, 8.9, hortifruti, true, false),

          // BEBIDAS (5 produtos)
          new GroceryProduct("Café Pilão", "Pilão", 500.0, UnitType.G, 100, 12.0, bebidas, true, false),
          new GroceryProduct("Coca-Cola", "Coca-Cola", 500.0, UnitType.ML, 250, 6.0, bebidas, true, false),
          new GroceryProduct("Água Mineral", "Crystal", 500.0, UnitType.ML, 300, 2.5, bebidas, true, false),
          new GroceryProduct("Suco de Laranja", "Del Valle", 1000.0, UnitType.ML, 80, 8.9, bebidas, true, false),
          new GroceryProduct("Cerveja Skol", "Ambev", 350.0, UnitType.ML, 200, 3.8, bebidas, true, false),

          // PRODUTOS BÁSICOS (5 produtos)
          new GroceryProduct("Arroz Branco", "Camil", 1000.0, UnitType.G, 300, 6.0, graos, true, false),
          new GroceryProduct("Feijão Carioca", "Camil", 1000.0, UnitType.G, 200, 8.5, graos, true, false),
          new GroceryProduct("Açúcar Cristal", "União", 1000.0, UnitType.G, 150, 4.2, graos, true, false),
          new GroceryProduct("Leite Integral", "Italac", 1000.0, UnitType.ML, 200, 4.5, laticinios, true, false),
          new GroceryProduct("Detergente", "Ypê", 500.0, UnitType.ML, 150, 2.8, limpeza, true, false),

          // PRODUTOS ADICIONAIS (10 produtos)
          new GroceryProduct("Pão de Forma", "Plus Vita", 500.0, UnitType.G, 80, 4.9, graos, true, false),
          new GroceryProduct("Margarina", "Qualy", 500.0, UnitType.G, 100, 5.8, laticinios, true, false),
          new GroceryProduct("Iogurte Natural", "Danone", 170.0, UnitType.G, 120, 3.2, laticinios, true, false),
          new GroceryProduct("Azeite Extra Virgem", "Andorinha", 500.0, UnitType.ML, 60, 18.9, bebidas, true, false),
          new GroceryProduct("Macarrão Espaguete", "Barilla", 500.0, UnitType.G, 200, 4.5, graos, true, false),
          new GroceryProduct("Sabão em Pó", "OMO", 1000.0, UnitType.G, 80, 12.9, limpeza, true, false),
          new GroceryProduct("Queijo Mussarela", "Tirolez", 200.0, UnitType.G, 90, 8.9, laticinios, true, false),
          new GroceryProduct("Papel Higiênico", "Neve", 4.0, UnitType.UN, 150, 9.8, limpeza, true, false),
          new GroceryProduct("Biscoito Cream Cracker", "Adria", 200.0, UnitType.G, 100, 3.4, graos, true, false),
          new GroceryProduct("Refrigerante Guaraná", "Antarctica", 2000.0, UnitType.ML, 80, 5.9, bebidas, true, false));

      // Definir data de validade para alguns produtos
      groceryProducts.get(0).setExpirationDate(LocalDate.now().plusDays(15)); // Frango
      groceryProducts.get(2).setExpirationDate(LocalDate.now().plusDays(10)); // Peito de frango
      groceryProducts.get(18).setExpirationDate(LocalDate.now().plusDays(30)); // Leite

      groceryProductRepository.saveAll(groceryProducts);
      System.out.println("✅ Produtos de supermercado inseridos! (30 itens)");
    }
  }

  /**
   * Seed do domínio MÓVEIS
   */
  private void seedFurnitureDomain() {
    System.out.println("🪑 Seeding domínio MÓVEIS...");

    if (categoryRepository.countByDomain(Domain.FURNITURE) == 0) {
      List<Category> furnitureCategories = Arrays.asList(
          new Category("Sala de Estar", Domain.FURNITURE),
          new Category("Quarto", Domain.FURNITURE),
          new Category("Cozinha", Domain.FURNITURE),
          new Category("Escritório", Domain.FURNITURE));

      categoryRepository.saveAll(furnitureCategories);
      System.out.println("✅ Categorias de móveis inseridas!");
    }

    if (furnitureProductRepository.count() == 0) {
      Category salaEstar = categoryRepository.findByNameAndDomain("Sala de Estar", Domain.FURNITURE).orElseThrow();
      Category quarto = categoryRepository.findByNameAndDomain("Quarto", Domain.FURNITURE).orElseThrow();
      Category cozinha = categoryRepository.findByNameAndDomain("Cozinha", Domain.FURNITURE).orElseThrow();
      Category escritorio = categoryRepository.findByNameAndDomain("Escritório", Domain.FURNITURE).orElseThrow();

      furnitureProductRepository.saveAll(Arrays.asList(
          // SALA DE ESTAR (4 produtos)
          new FurnitureProduct("Sofá 3 Lugares", "Tok&Stok", 1.0, UnitType.UN, 15, 899.99, salaEstar, true, false,
              "Tecido", "Azul"),
          new FurnitureProduct("Mesa de Centro", "MadeiraMadeira", 1.0, UnitType.UN, 25, 299.99, salaEstar, true, false,
              "Vidro", "Transparente"),
          new FurnitureProduct("Poltrona Reclinável", "La-Z-Boy", 1.0, UnitType.UN, 8, 1299.99, salaEstar, true, false,
              "Couro", "Marrom"),
          new FurnitureProduct("Estante TV", "Madesa", 1.0, UnitType.UN, 12, 189.99, salaEstar, true, false,
              "MDF", "Branco"),

          // QUARTO (4 produtos)
          new FurnitureProduct("Cama Box Queen", "Ortobom", 1.0, UnitType.UN, 12, 799.99, quarto, true, false,
              "Tecido", "Branco"),
          new FurnitureProduct("Guarda-roupa 6 Portas", "Casas Bahia", 1.0, UnitType.UN, 6, 599.99, quarto, true, false,
              "MDF", "Branco"),
          new FurnitureProduct("Cômoda 4 Gavetas", "Madesa", 1.0, UnitType.UN, 20, 189.99, quarto, true, false,
              "MDF", "Amadeirado"),
          new FurnitureProduct("Criado-mudo", "MadeiraMadeira", 1.0, UnitType.UN, 25, 129.99, quarto, true, false,
              "Madeira", "Pinus"),

          // COZINHA (3 produtos)
          new FurnitureProduct("Mesa de Jantar 6 Lugares", "Móveis Carraro", 1.0, UnitType.UN, 10, 449.99, cozinha,
              true, false, "Madeira", "Carvalho"),
          new FurnitureProduct("Cadeiras Estofadas (4x)", "Tramontina", 4.0, UnitType.UN, 20, 320.00, cozinha, true,
              false, "Tecido", "Bege"),
          new FurnitureProduct("Buffet Cozinha", "Madesa", 1.0, UnitType.UN, 8, 389.99, cozinha, true, false,
              "MDF", "Branco"),

          // ESCRITÓRIO (3 produtos)
          new FurnitureProduct("Mesa de Escritório", "Politorno", 1.0, UnitType.UN, 18, 249.99, escritorio, true, false,
              "MDF", "Branco"),
          new FurnitureProduct("Cadeira de Escritório", "DT3 Office", 1.0, UnitType.UN, 15, 379.99, escritorio, true,
              false, "Tecido", "Preto"),
          new FurnitureProduct("Estante para Livros", "Madesa", 1.0, UnitType.UN, 12, 189.99, escritorio, true, false,
              "MDF", "Amadeirado"),

          // PRODUTOS ADICIONAIS (10 produtos)
          new FurnitureProduct("Rack para TV 55'", "Móveis Bechara", 1.0, UnitType.UN, 15, 349.99, salaEstar, true, false,
              "MDF", "Preto"),
          new FurnitureProduct("Puff Redondo", "Divaloto", 1.0, UnitType.UN, 30, 89.99, salaEstar, true, false,
              "Tecido", "Verde"),
          new FurnitureProduct("Penteadeira com Espelho", "JB Bechara", 1.0, UnitType.UN, 8, 299.99, quarto, true, false,
              "MDF", "Branco"),
          new FurnitureProduct("Beliche Solteiro", "Santos Andirá", 1.0, UnitType.UN, 5, 699.99, quarto, true, false,
              "Madeira", "Pinus"),
          new FurnitureProduct("Aparador de Sala", "Móveis Carraro", 1.0, UnitType.UN, 12, 259.99, cozinha, true, false,
              "Madeira", "Carvalho"),
          new FurnitureProduct("Banquetas Altas (2x)", "Tok&Stok", 2.0, UnitType.UN, 20, 179.99, cozinha, true, false,
              "Metal", "Preto"),
          new FurnitureProduct("Gaveteiro 3 Gavetas", "Politorno", 1.0, UnitType.UN, 18, 149.99, escritorio, true, false,
              "MDF", "Carvalho"),
          new FurnitureProduct("Poltrona para Escritório", "Flexform", 1.0, UnitType.UN, 10, 459.99, escritorio, true, false,
              "Couro", "Preto"),
          new FurnitureProduct("Mesa Lateral", "MadeiraMadeira", 1.0, UnitType.UN, 25, 119.99, salaEstar, true, false,
              "Vidro", "Fumê"),
          new FurnitureProduct("Armário Aéreo Cozinha", "Itatiaia", 1.0, UnitType.UN, 14, 229.99, cozinha, true, false,
              "Aço", "Branco")));

      System.out.println("✅ Produtos de móveis inseridos! (24 itens)");
    }
  }

  /**
   * Seed do domínio CONSTRUÇÃO
   */
  private void seedConstructionDomain() {
    System.out.println("🔨 Seeding domínio CONSTRUÇÃO...");

    if (categoryRepository.countByDomain(Domain.CONSTRUCTION) == 0) {
      List<Category> constructionCategories = Arrays.asList(
          new Category("Acabamento", Domain.CONSTRUCTION),
          new Category("Estrutural", Domain.CONSTRUCTION),
          new Category("Hidráulica", Domain.CONSTRUCTION),
          new Category("Elétrica", Domain.CONSTRUCTION));

      categoryRepository.saveAll(constructionCategories);
      System.out.println("✅ Categorias de construção inseridas!");
    }

    if (constructionProductRepository.count() == 0) {
      Category acabamento = categoryRepository.findByNameAndDomain("Acabamento", Domain.CONSTRUCTION).orElseThrow();
      Category estrutural = categoryRepository.findByNameAndDomain("Estrutural", Domain.CONSTRUCTION).orElseThrow();
      Category hidraulica = categoryRepository.findByNameAndDomain("Hidráulica", Domain.CONSTRUCTION).orElseThrow();
      Category eletrica = categoryRepository.findByNameAndDomain("Elétrica", Domain.CONSTRUCTION).orElseThrow();

      constructionProductRepository.saveAll(Arrays.asList(
          // ACABAMENTO (4 produtos)
          new ConstructionProduct("Tinta Látex Branca 18L", "Suvinil", 18.0, UnitType.L, 50, 89.99, acabamento, true,
              false, "Tinta látex PVA para paredes internas"),
          new ConstructionProduct("Piso Cerâmico 45x45cm", "Portobello", 1.8, UnitType.M2, 200, 19.99, acabamento, true,
              false, "Cerâmica esmaltada para ambientes internos"),
          new ConstructionProduct("Azulejo Branco 20x30cm", "Cecrisa", 0.6, UnitType.M2, 300, 12.99, acabamento, true,
              false, "Azulejo esmaltado para banheiros e cozinhas"),
          new ConstructionProduct("Rejunte Cinza 1kg", "Quartzolit", 1.0, UnitType.KG, 100, 8.99, acabamento, true,
              false, "Rejunte flexível para pisos e azulejos"),

          // ESTRUTURAL (4 produtos)
          new ConstructionProduct("Cimento Portland 50kg", "Votorantim", 50.0, UnitType.KG, 100, 24.99, estrutural,
              true, false, "Cimento CP-II para construção geral"),
          new ConstructionProduct("Ferro 8mm 12m", "Gerdau", 12.0, UnitType.M, 120, 28.99, estrutural, true, false,
              "Vergalhão CA-50 para estruturas de concreto"),
          new ConstructionProduct("Areia Média", "Mineração", 1000.0, UnitType.KG, 40, 45.00, estrutural, true, false,
              "Areia lavada para concreto e argamassa"),
          new ConstructionProduct("Bloco de Concreto 14x19x39", "Tatu", 1.0, UnitType.UN, 500, 2.99, estrutural, true,
              false, "Bloco estrutural para alvenaria"),

          // HIDRÁULICA (3 produtos)
          new ConstructionProduct("Tubo PVC 100mm", "Tigre", 6.0, UnitType.M, 80, 15.99, hidraulica, true, false,
              "Tubo PVC para esgoto predial"),
          new ConstructionProduct("Torneira Parede", "Deca", 0.5, UnitType.UN, 30, 45.99, hidraulica, true, false,
              "Torneira cromada para jardim"),
          new ConstructionProduct("Vaso Sanitário", "Deca", 1.0, UnitType.UN, 15, 189.99, hidraulica, true, false,
              "Vaso sanitário com caixa acoplada"),

          // ELÉTRICA (3 produtos)
          new ConstructionProduct("Fio Elétrico 2,5mm", "Prysmian", 100.0, UnitType.M, 60, 89.99, eletrica, true,
              false, "Cabo flexível para instalações prediais"),
          new ConstructionProduct("Disjuntor 20A", "Siemens", 0.1, UnitType.UN, 100, 12.99, eletrica, true, false,
              "Disjuntor monopolar para quadro elétrico"),
          new ConstructionProduct("Tomada 2P+T 10A", "Tramontina", 0.05, UnitType.UN, 150, 8.99, eletrica, true, false,
              "Tomada padrão brasileiro"),

          // PRODUTOS ADICIONAIS (10 produtos)
          new ConstructionProduct("Telha Cerâmica", "Ceramica City", 1.0, UnitType.UN, 500, 1.89, acabamento, true, false,
              "Telha colonial para telhados"),
          new ConstructionProduct("Porta de Madeira 80cm", "Madecol", 1.0, UnitType.UN, 20, 189.99, acabamento, true, false,
              "Porta semi-oca com fechadura"),
          new ConstructionProduct("Janela de Alumínio 100x80", "Sasazaki", 1.0, UnitType.UN, 15, 259.99, acabamento, true, false,
              "Janela de correr com vidro"),
          new ConstructionProduct("Brita 1", "Pedreira Central", 1000.0, UnitType.KG, 30, 35.00, estrutural, true, false,
              "Brita graduada para concreto"),
          new ConstructionProduct("Cal Hidratada 20kg", "Votorantim", 20.0, UnitType.KG, 80, 8.99, estrutural, true, false,
              "Cal para argamassa e pintura"),
          new ConstructionProduct("Chuveiro Elétrico", "Corona", 1.0, UnitType.UN, 25, 79.99, hidraulica, true, false,
              "Chuveiro 4400W com regulagem"),
          new ConstructionProduct("Registro de Gaveta 3/4", "Tigre", 1.0, UnitType.UN, 40, 15.99, hidraulica, true, false,
              "Registro para controle de água"),
          new ConstructionProduct("Interruptor Simples", "Tramontina", 0.02, UnitType.UN, 200, 4.99, eletrica, true, false,
              "Interruptor uma tecla 10A"),
          new ConstructionProduct("Lâmpada LED 12W", "Philips", 0.05, UnitType.UN, 100, 15.99, eletrica, true, false,
              "Lâmpada LED branca fria"),
          new ConstructionProduct("Caixa d'água 500L", "Fortlev", 500.0, UnitType.L, 10, 189.99, hidraulica, true, false,
              "Caixa d'água de polietileno")));

      System.out.println("✅ Produtos de construção inseridos! (24 itens)");
    }
  }
}
