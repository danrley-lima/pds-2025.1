package com.danrley.product_management.common.config;

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
 * Seeder multi-domínio que popula o banco com dados para todos os domínios
 * suportados.
 */
@Component
@Profile({ "dev", "test" })
@RequiredArgsConstructor
@Order(5) // Executa após todos os registros de domínio
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

    System.out.println("🚀 Iniciando seed multi-domínio...");

    // Seeder por domínio
    seedGroceryDomain();
    seedFurnitureDomain();
    seedConstructionDomain();

    // Promoções
    seedPromotions();

    System.out.println("✅ Seed multi-domínio concluído!");
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
          new Category("Temperos", Domain.GROCERY),
          new Category("Enlatados", Domain.GROCERY),
          new Category("Laticínios", Domain.GROCERY),
          new Category("Bebidas", Domain.GROCERY),
          new Category("Padaria", Domain.GROCERY),
          new Category("Produtos de limpeza", Domain.GROCERY),
          new Category("Grãos e Cereais", Domain.GROCERY),
          new Category("Óleos e Gorduras", Domain.GROCERY),
          new Category("Massas", Domain.GROCERY),
          new Category("Doces e Sobremesas", Domain.GROCERY));

      categoryRepository.saveAll(groceryCategories);
      System.out.println("✅ Categorias de supermercado inseridas!");
    }

    if (groceryProductRepository.count() == 0) {
      Category carnes = categoryRepository.findByNameAndDomain("Carnes", Domain.GROCERY).orElseThrow();
      Category hortifruti = categoryRepository.findByNameAndDomain("Hortifruti", Domain.GROCERY).orElseThrow();
      Category temperos = categoryRepository.findByNameAndDomain("Temperos", Domain.GROCERY).orElseThrow();
      Category bebidas = categoryRepository.findByNameAndDomain("Bebidas", Domain.GROCERY).orElseThrow();
      Category laticinios = categoryRepository.findByNameAndDomain("Laticínios", Domain.GROCERY).orElseThrow();
      Category enlatados = categoryRepository.findByNameAndDomain("Enlatados", Domain.GROCERY).orElseThrow();
      Category padaria = categoryRepository.findByNameAndDomain("Padaria", Domain.GROCERY).orElseThrow();
      Category graos = categoryRepository.findByNameAndDomain("Grãos e Cereais", Domain.GROCERY).orElseThrow();
      Category limpeza = categoryRepository.findByNameAndDomain("Produtos de limpeza", Domain.GROCERY).orElseThrow();
      Category oleos = categoryRepository.findByNameAndDomain("Óleos e Gorduras", Domain.GROCERY).orElseThrow();
      Category massas = categoryRepository.findByNameAndDomain("Massas", Domain.GROCERY).orElseThrow();
      Category doces = categoryRepository.findByNameAndDomain("Doces e Sobremesas", Domain.GROCERY).orElseThrow();

      groceryProductRepository.saveAll(Arrays.asList(
          // CARNES (15 produtos)
          new GroceryProduct("Frango em Cubos", "Sadia", 500.0, UnitType.G, 50, 18.0, carnes, true, false),
          new GroceryProduct("Carne Moída", "Friboi", 1000.0, UnitType.G, 100, 22.0, carnes, true, false),
          new GroceryProduct("Peito de Frango", "Sadia", 1000.0, UnitType.G, 80, 15.5, carnes, true, false),
          new GroceryProduct("Costela Bovina", "Seara", 1000.0, UnitType.G, 30, 35.0, carnes, true, false),
          new GroceryProduct("Linguiça Toscana", "Perdigão", 500.0, UnitType.G, 40, 12.9, carnes, true, false),
          new GroceryProduct("Coxinha da Asa", "Aurora", 1000.0, UnitType.G, 60, 8.9, carnes, true, false),
          new GroceryProduct("Picanha", "Friboi", 1000.0, UnitType.G, 20, 89.9, carnes, true, false),
          new GroceryProduct("Filé de Peixe", "Captain", 500.0, UnitType.G, 25, 24.9, carnes, true, false),
          new GroceryProduct("Bacon Fatiado", "Sadia", 250.0, UnitType.G, 35, 13.5, carnes, true, false),
          new GroceryProduct("Ovos Brancos", "Korin", 60.0, UnitType.UN, 200, 0.85, carnes, true, false),
          new GroceryProduct("Salsicha Hot Dog", "Perdigão", 500.0, UnitType.G, 45, 7.9, carnes, true, false),
          new GroceryProduct("Hambúrguer Bovino", "Seara", 800.0, UnitType.G, 30, 16.9, carnes, true, false),
          new GroceryProduct("Coxa e Sobrecoxa", "Sadia", 1000.0, UnitType.G, 70, 11.9, carnes, true, false),
          new GroceryProduct("Carne de Porco", "Aurora", 1000.0, UnitType.G, 25, 19.9, carnes, true, false),
          new GroceryProduct("Asa de Frango", "Perdigão", 1000.0, UnitType.G, 55, 9.9, carnes, true, false),

          // HORTIFRUTI (20 produtos)
          new GroceryProduct("Cebola", "Natural", 100.0, UnitType.G, 200, 3.5, hortifruti, true, false),
          new GroceryProduct("Tomate", "Agrícola", 200.0, UnitType.G, 150, 4.0, hortifruti, true, false),
          new GroceryProduct("Batata Inglesa", "Natural", 1000.0, UnitType.G, 180, 5.9, hortifruti, true, false),
          new GroceryProduct("Cenoura", "Orgânica", 500.0, UnitType.G, 120, 4.5, hortifruti, true, false),
          new GroceryProduct("Alface Americana", "Hidropônica", 300.0, UnitType.G, 80, 3.2, hortifruti, true, false),
          new GroceryProduct("Banana Prata", "Natural", 1000.0, UnitType.G, 100, 6.8, hortifruti, true, false),
          new GroceryProduct("Maçã Fuji", "Nacional", 1000.0, UnitType.G, 90, 8.9, hortifruti, true, false),
          new GroceryProduct("Laranja Pera", "Natural", 1000.0, UnitType.G, 110, 4.5, hortifruti, true, false),
          new GroceryProduct("Limão Tahiti", "Natural", 500.0, UnitType.G, 85, 3.8, hortifruti, true, false),
          new GroceryProduct("Abacaxi Pérola", "Natural", 1000.0, UnitType.G, 40, 7.9, hortifruti, true, false),
          new GroceryProduct("Mamão Papaya", "Natural", 1000.0, UnitType.G, 50, 5.5, hortifruti, true, false),
          new GroceryProduct("Pimentão Verde", "Natural", 200.0, UnitType.G, 70, 8.9, hortifruti, true, false),
          new GroceryProduct("Abobrinha", "Natural", 500.0, UnitType.G, 60, 4.2, hortifruti, true, false),
          new GroceryProduct("Berinjela", "Natural", 500.0, UnitType.G, 45, 5.8, hortifruti, true, false),
          new GroceryProduct("Brócolis", "Orgânico", 400.0, UnitType.G, 35, 6.5, hortifruti, true, false),
          new GroceryProduct("Couve-flor", "Natural", 800.0, UnitType.G, 30, 4.9, hortifruti, true, false),
          new GroceryProduct("Repolho Verde", "Natural", 1000.0, UnitType.G, 40, 3.8, hortifruti, true, false),
          new GroceryProduct("Uva Rubi", "Nacional", 500.0, UnitType.G, 25, 12.9, hortifruti, true, false),
          new GroceryProduct("Manga Tommy", "Natural", 500.0, UnitType.G, 35, 8.5, hortifruti, true, false),
          new GroceryProduct("Melancia", "Natural", 2000.0, UnitType.G, 20, 6.9, hortifruti, true, false),

          // BEBIDAS (15 produtos)
          new GroceryProduct("Café Pilão", "Pilão", 500.0, UnitType.G, 100, 12.0, bebidas, true, false),
          new GroceryProduct("Coca-Cola", "Coca-Cola", 500.0, UnitType.ML, 250, 6.0, bebidas, true, false),
          new GroceryProduct("Água Mineral", "Crystal", 500.0, UnitType.ML, 300, 2.5, bebidas, true, false),
          new GroceryProduct("Suco de Laranja", "Del Valle", 1000.0, UnitType.ML, 80, 8.9, bebidas, true, false),
          new GroceryProduct("Cerveja Skol", "Ambev", 350.0, UnitType.ML, 200, 3.8, bebidas, true, false),
          new GroceryProduct("Refrigerante Guaraná", "Antarctica", 350.0, UnitType.ML, 150, 3.5, bebidas, true, false),
          new GroceryProduct("Chá Mate Leão", "Leão", 250.0, UnitType.G, 60, 5.9, bebidas, true, false),
          new GroceryProduct("Energético Red Bull", "Red Bull", 250.0, UnitType.ML, 50, 8.9, bebidas, true, false),
          new GroceryProduct("Vinho Tinto", "Miolo", 750.0, UnitType.ML, 30, 24.9, bebidas, true, false),
          new GroceryProduct("Leite de Coco", "Ducoco", 200.0, UnitType.ML, 40, 3.2, bebidas, true, false),
          new GroceryProduct("Café Solúvel", "Nescafé", 100.0, UnitType.G, 70, 9.5, bebidas, true, false),
          new GroceryProduct("Isotônico Gatorade", "Pepsico", 500.0, UnitType.ML, 60, 4.9, bebidas, true, false),
          new GroceryProduct("Cerveja Heineken", "Heineken", 330.0, UnitType.ML, 80, 5.9, bebidas, true, false),
          new GroceryProduct("Achocolatado Nescau", "Nestlé", 400.0, UnitType.G, 45, 7.8, bebidas, true, false),
          new GroceryProduct("Suco de Uva", "Aurora", 1000.0, UnitType.ML, 35, 6.5, bebidas, true, false),

          // OUTROS ITENS ESSENCIAIS
          new GroceryProduct("Arroz Branco", "Camil", 1000.0, UnitType.G, 300, 6.0, graos, true, false),
          new GroceryProduct("Feijão Carioca", "Camil", 1000.0, UnitType.G, 200, 8.5, graos, true, false),
          new GroceryProduct("Açúcar Cristal", "União", 1000.0, UnitType.G, 150, 4.2, graos, true, false),
          new GroceryProduct("Sal Refinado", "Cisne", 1000.0, UnitType.G, 300, 1.8, temperos, true, false),
          new GroceryProduct("Alho", "Natural", 50.0, UnitType.G, 100, 2.0, temperos, true, false),
          new GroceryProduct("Leite Integral", "Italac", 1000.0, UnitType.ML, 200, 4.5, laticinios, true, false),
          new GroceryProduct("Pão Francês", "Padaria", 50.0, UnitType.G, 500, 0.8, padaria, true, false),
          new GroceryProduct("Óleo de Soja", "Liza", 900.0, UnitType.ML, 120, 7.9, oleos, true, false),
          new GroceryProduct("Macarrão Espaguete", "Barilla", 500.0, UnitType.G, 100, 4.8, massas, true, false),
          new GroceryProduct("Extrato de Tomate", "Elefante", 130.0, UnitType.G, 80, 3.2, enlatados, true, false),
          new GroceryProduct("Detergente", "Ypê", 500.0, UnitType.ML, 150, 2.8, limpeza, true, false),
          new GroceryProduct("Chocolate ao Leite", "Nestlé", 90.0, UnitType.G, 60, 4.5, doces, true, false)));

      System.out.println("✅ Produtos de supermercado inseridos! (45+ itens)");
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
          new Category("Escritório", Domain.FURNITURE),
          new Category("Decoração", Domain.FURNITURE),
          new Category("Área Externa", Domain.FURNITURE));

      categoryRepository.saveAll(furnitureCategories);
      System.out.println("✅ Categorias de móveis inseridas!");
    }

    // For furniture domain, check if any furniture products exist
    if (furnitureProductRepository.count() == 0) {
      Category salaEstar = categoryRepository.findByNameAndDomain("Sala de Estar", Domain.FURNITURE).orElseThrow();
      Category quarto = categoryRepository.findByNameAndDomain("Quarto", Domain.FURNITURE).orElseThrow();
      Category cozinha = categoryRepository.findByNameAndDomain("Cozinha", Domain.FURNITURE).orElseThrow();
      Category escritorio = categoryRepository.findByNameAndDomain("Escritório", Domain.FURNITURE).orElseThrow();
      Category decoracao = categoryRepository.findByNameAndDomain("Decoração", Domain.FURNITURE).orElseThrow();
      Category areaExterna = categoryRepository.findByNameAndDomain("Área Externa", Domain.FURNITURE).orElseThrow();

      furnitureProductRepository.saveAll(Arrays.asList(
          // SALA DE ESTAR (15 produtos)
          new FurnitureProduct("Sofá 3 Lugares", "Tok&Stok", 1.0, UnitType.UN, 15, 899.99, salaEstar, true, false,
              "200x90x80", "Tecido", "Azul", "Moderno"),
          new FurnitureProduct("Mesa de Centro Vidro", "MadeiraMadeira", 1.0, UnitType.UN, 25, 299.99, salaEstar, true,
              false, "120x60x40", "Vidro", "Transparente", "Moderno"),
          new FurnitureProduct("Poltrona Reclinável", "La-Z-Boy", 1.0, UnitType.UN, 8, 1299.99, salaEstar, true, false,
              "90x85x100", "Couro", "Marrom", "Clássico"),
          new FurnitureProduct("Sofá 2 Lugares", "Casas Bahia", 1.0, UnitType.UN, 20, 599.99, salaEstar, true, false,
              "150x90x80", "Tecido", "Cinza", "Moderno"),
          new FurnitureProduct("Mesa de Centro Madeira", "Móveis Carraro", 1.0, UnitType.UN, 18, 249.99, salaEstar,
              true, false, "100x50x45", "Madeira", "Imbuia", "Rústico"),
          new FurnitureProduct("Estante TV 55\"", "Madesa", 1.0, UnitType.UN, 12, 189.99, salaEstar, true, false,
              "140x35x60", "MDF", "Branco", "Moderno"),
          new FurnitureProduct("Poltrona Decorativa", "Tok&Stok", 1.0, UnitType.UN, 10, 799.99, salaEstar, true, false,
              "80x75x85", "Veludo", "Rosa", "Contemporâneo"),
          new FurnitureProduct("Mesa Lateral", "MadeiraMadeira", 1.0, UnitType.UN, 30, 129.99, salaEstar, true, false,
              "50x50x60", "Madeira", "Carvalho", "Moderno"),
          new FurnitureProduct("Sofá Canto Esquerdo", "Etna", 1.0, UnitType.UN, 5, 1599.99, salaEstar, true, false,
              "250x180x85", "Tecido", "Bege", "Moderno"),
          new FurnitureProduct("Puff Decorativo", "Tok&Stok", 1.0, UnitType.UN, 25, 159.99, salaEstar, true, false,
              "40x40x40", "Tecido", "Verde", "Contemporâneo"),
          new FurnitureProduct("Mesa de Centro Oval", "Móveis Carraro", 1.0, UnitType.UN, 15, 349.99, salaEstar, true,
              false, "110x70x45", "Madeira", "Mogno", "Clássico"),
          new FurnitureProduct("Sofá Chaise Longue", "Casas Bahia", 1.0, UnitType.UN, 8, 1099.99, salaEstar, true,
              false, "220x90x80", "Tecido", "Preto", "Moderno"),
          new FurnitureProduct("Estante Modulada", "Madesa", 1.0, UnitType.UN, 12, 459.99, salaEstar, true, false,
              "180x35x180", "MDF", "Amadeirado", "Moderno"),
          new FurnitureProduct("Mesa de Apoio", "MadeiraMadeira", 1.0, UnitType.UN, 20, 89.99, salaEstar, true, false,
              "35x35x65", "Metal", "Preto", "Industrial"),
          new FurnitureProduct("Rack TV Suspenso", "Politorno", 1.0, UnitType.UN, 16, 219.99, salaEstar, true, false,
              "120x30x30", "MDF", "Branco", "Moderno"),

          // QUARTO (15 produtos)
          new FurnitureProduct("Cama Box Queen", "Ortobom", 1.0, UnitType.UN, 12, 799.99, quarto, true, false,
              "158x198x58", "Tecido", "Branco", "Clássico"),
          new FurnitureProduct("Guarda-roupa 6 Portas", "Casas Bahia", 1.0, UnitType.UN, 6, 599.99, quarto, true, false,
              "274x60x218", "MDF", "Branco", "Moderno"),
          new FurnitureProduct("Cama Box Solteiro", "Ortobom", 1.0, UnitType.UN, 15, 499.99, quarto, true, false,
              "88x188x58", "Tecido", "Azul", "Moderno"),
          new FurnitureProduct("Cômoda 4 Gavetas", "Madesa", 1.0, UnitType.UN, 20, 189.99, quarto, true, false,
              "79x40x108", "MDF", "Amadeirado", "Moderno"),
          new FurnitureProduct("Criado-mudo", "MadeiraMadeira", 1.0, UnitType.UN, 25, 129.99, quarto, true, false,
              "40x35x60", "Madeira", "Pinus", "Rústico"),
          new FurnitureProduct("Guarda-roupa 4 Portas", "Móveis Carraro", 1.0, UnitType.UN, 8, 449.99, quarto, true,
              false, "183x60x218", "MDF", "Carvalho", "Moderno"),
          new FurnitureProduct("Cama King Size", "Ortobom", 1.0, UnitType.UN, 6, 1199.99, quarto, true, false,
              "193x203x58", "Couro", "Preto", "Contemporâneo"),
          new FurnitureProduct("Penteadeira com Espelho", "Tok&Stok", 1.0, UnitType.UN, 10, 349.99, quarto, true, false,
              "100x45x160", "MDF", "Branco", "Clássico"),
          new FurnitureProduct("Colchão Queen Mola", "Ortobom", 1.0, UnitType.UN, 15, 899.99, quarto, true, false,
              "158x198x30", "Tecido", "Branco", "Moderno"),
          new FurnitureProduct("Escrivaninha Quarto", "Madesa", 1.0, UnitType.UN, 18, 199.99, quarto, true, false,
              "120x60x75", "MDF", "Rosa", "Moderno"),
          new FurnitureProduct("Roupeiro 8 Portas", "Casas Bahia", 1.0, UnitType.UN, 4, 799.99, quarto, true, false,
              "365x60x218", "MDF", "Amadeirado", "Moderno"),
          new FurnitureProduct("Cama Auxiliar", "MadeiraMadeira", 1.0, UnitType.UN, 12, 299.99, quarto, true, false,
              "88x188x30", "Metal", "Branco", "Moderno"),
          new FurnitureProduct("Sapateira 20 Pares", "Politorno", 1.0, UnitType.UN, 22, 149.99, quarto, true, false,
              "55x30x176", "MDF", "Espelhado", "Moderno"),
          new FurnitureProduct("Colchão Solteiro", "Ortobom", 1.0, UnitType.UN, 20, 399.99, quarto, true, false,
              "88x188x25", "Tecido", "Azul", "Moderno"),
          new FurnitureProduct("Baú Organizador", "Madesa", 1.0, UnitType.UN, 15, 179.99, quarto, true, false,
              "90x45x45", "Tecido", "Cinza", "Moderno"),

          // COZINHA (12 produtos)
          new FurnitureProduct("Mesa de Jantar 6 Lugares", "Móveis Carraro", 1.0, UnitType.UN, 10, 449.99, cozinha,
              true, false, "160x90x75", "Madeira", "Carvalho", "Rústico"),
          new FurnitureProduct("Cadeiras Estofadas (4x)", "Tramontina", 4.0, UnitType.UN, 20, 320.00, cozinha, true,
              false, "45x55x80", "Tecido", "Bege", "Clássico"),
          new FurnitureProduct("Mesa de Jantar 4 Lugares", "Casas Bahia", 1.0, UnitType.UN, 15, 299.99, cozinha, true,
              false, "120x80x75", "Vidro", "Transparente", "Moderno"),
          new FurnitureProduct("Buffet Cozinha", "Madesa", 1.0, UnitType.UN, 8, 389.99, cozinha, true, false,
              "166x40x86", "MDF", "Branco", "Moderno"),
          new FurnitureProduct("Banquetas Bar (2x)", "Tok&Stok", 2.0, UnitType.UN, 25, 189.99, cozinha, true, false,
              "35x35x75", "Metal", "Preto", "Industrial"),
          new FurnitureProduct("Mesa Dobrável Parede", "MadeiraMadeira", 1.0, UnitType.UN, 18, 159.99, cozinha, true,
              false, "80x50x75", "Madeira", "Pinus", "Moderno"),
          new FurnitureProduct("Armário Aéreo Cozinha", "Politorno", 1.0, UnitType.UN, 12, 249.99, cozinha, true, false,
              "120x30x70", "MDF", "Branco", "Moderno"),
          new FurnitureProduct("Carrinho de Cozinha", "Tramontina", 1.0, UnitType.UN, 20, 179.99, cozinha, true, false,
              "60x40x85", "Metal", "Inox", "Moderno"),
          new FurnitureProduct("Mesa Bancada Cozinha", "Madesa", 1.0, UnitType.UN, 10, 329.99, cozinha, true, false,
              "120x60x90", "Granito", "Preto", "Contemporâneo"),
          new FurnitureProduct("Cadeira Plástica (6x)", "Tramontina", 6.0, UnitType.UN, 30, 149.99, cozinha, true,
              false, "45x55x80", "Plástico", "Branco", "Moderno"),
          new FurnitureProduct("Fruteira de Mesa", "Tok&Stok", 1.0, UnitType.UN, 40, 49.99, cozinha, true, false,
              "30x30x15", "Madeira", "Natural", "Rústico"),
          new FurnitureProduct("Mesa Redonda 4 Lugares", "Móveis Carraro", 1.0, UnitType.UN, 12, 379.99, cozinha, true,
              false, "120x120x75", "Madeira", "Mogno", "Clássico"),

          // ESCRITÓRIO (10 produtos)
          new FurnitureProduct("Mesa de Escritório", "Politorno", 1.0, UnitType.UN, 18, 249.99, escritorio, true, false,
              "120x60x75", "MDF", "Branco", "Moderno"),
          new FurnitureProduct("Cadeira de Escritório", "DT3 Office", 1.0, UnitType.UN, 15, 379.99, escritorio, true,
              false, "65x65x110", "Tecido", "Preto", "Ergonômico"),
          new FurnitureProduct("Estante para Livros", "Madesa", 1.0, UnitType.UN, 12, 189.99, escritorio, true, false,
              "80x30x180", "MDF", "Amadeirado", "Moderno"),
          new FurnitureProduct("Mesa L Escritório", "Tok&Stok", 1.0, UnitType.UN, 10, 449.99, escritorio, true, false,
              "150x120x75", "MDF", "Branco", "Moderno"),
          new FurnitureProduct("Cadeira Gamer", "DT3 Office", 1.0, UnitType.UN, 8, 599.99, escritorio, true, false,
              "70x70x125", "Couro Sintético", "Preto", "Gamer"),
          new FurnitureProduct("Arquivo 4 Gavetas", "Politorno", 1.0, UnitType.UN, 15, 299.99, escritorio, true, false,
              "40x60x132", "Metal", "Cinza", "Corporativo"),
          new FurnitureProduct("Mesa para Notebook", "MadeiraMadeira", 1.0, UnitType.UN, 25, 89.99, escritorio, true,
              false, "60x40x75", "MDF", "Amadeirado", "Moderno"),
          new FurnitureProduct("Estante Modulada Home", "Madesa", 1.0, UnitType.UN, 12, 329.99, escritorio, true, false,
              "164x30x180", "MDF", "Branco", "Moderno"),
          new FurnitureProduct("Cadeira Diretor", "Politorno", 1.0, UnitType.UN, 10, 449.99, escritorio, true, false,
              "70x70x115", "Couro", "Preto", "Executivo"),
          new FurnitureProduct("Bancada para PC", "Casas Bahia", 1.0, UnitType.UN, 20, 199.99, escritorio, true, false,
              "135x60x75", "MDF", "Preto", "Gamer"),

          // DECORAÇÃO (8 produtos)
          new FurnitureProduct("Luminária de Chão", "Tok&Stok", 1.0, UnitType.UN, 15, 189.99, decoracao, true, false,
              "25x25x150", "Metal", "Dourado", "Contemporâneo"),
          new FurnitureProduct("Espelho Decorativo", "MadeiraMadeira", 1.0, UnitType.UN, 20, 129.99, decoracao, true,
              false, "60x80x3", "Vidro", "Espelhado", "Moderno"),
          new FurnitureProduct("Tapete Sala 200x140", "Tok&Stok", 1.0, UnitType.UN, 12, 249.99, decoracao, true, false,
              "200x140x1", "Tecido", "Bege", "Contemporâneo"),
          new FurnitureProduct("Luminária de Mesa", "Casas Bahia", 1.0, UnitType.UN, 25, 79.99, decoracao, true, false,
              "20x20x35", "Cerâmica", "Branco", "Moderno"),
          new FurnitureProduct("Vaso Decorativo Grande", "MadeiraMadeira", 1.0, UnitType.UN, 18, 99.99, decoracao, true,
              false, "30x30x60", "Cerâmica", "Azul", "Contemporâneo"),
          new FurnitureProduct("Quadro Decorativo", "Tok&Stok", 1.0, UnitType.UN, 30, 59.99, decoracao, true, false,
              "40x60x3", "Canvas", "Colorido", "Moderno"),
          new FurnitureProduct("Tapete Quarto 150x100", "Madesa", 1.0, UnitType.UN, 20, 89.99, decoracao, true, false,
              "150x100x1", "Tecido", "Rosa", "Moderno"),
          new FurnitureProduct("Luminária Pendente", "Politorno", 1.0, UnitType.UN, 15, 149.99, decoracao, true, false,
              "30x30x40", "Vidro", "Transparente", "Industrial"),

          // ÁREA EXTERNA (5 produtos)
          new FurnitureProduct("Mesa Área Externa", "Tramontina", 1.0, UnitType.UN, 10, 299.99, areaExterna, true,
              false, "140x80x75", "Alumínio", "Branco", "Moderno"),
          new FurnitureProduct("Cadeiras Área Externa (4x)", "Tramontina", 4.0, UnitType.UN, 15, 199.99, areaExterna,
              true, false, "55x55x80", "Alumínio", "Branco", "Moderno"),
          new FurnitureProduct("Espreguiçadeira", "Mor", 1.0, UnitType.UN, 20, 159.99, areaExterna, true, false,
              "60x190x35", "Alumínio", "Azul", "Moderno"),
          new FurnitureProduct("Guarda-sol", "Mor", 1.0, UnitType.UN, 12, 129.99, areaExterna, true, false,
              "240x240x250", "Tecido", "Listrado", "Moderno"),
          new FurnitureProduct("Banco de Jardim", "Tramontina", 1.0, UnitType.UN, 8, 189.99, areaExterna, true, false,
              "120x45x45", "Madeira", "Teca", "Rústico")));

      System.out.println("✅ Produtos de móveis inseridos! (65+ itens)");
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
          new Category("Elétrica", Domain.CONSTRUCTION),
          new Category("Ferramentas", Domain.CONSTRUCTION),
          new Category("Segurança", Domain.CONSTRUCTION));

      categoryRepository.saveAll(constructionCategories);
      System.out.println("✅ Categorias de construção inseridas!");
    }

    // For construction domain, check if any construction products exist
    if (constructionProductRepository.count() == 0) {
      Category acabamento = categoryRepository.findByNameAndDomain("Acabamento", Domain.CONSTRUCTION).orElseThrow();
      Category estrutural = categoryRepository.findByNameAndDomain("Estrutural", Domain.CONSTRUCTION).orElseThrow();
      Category hidraulica = categoryRepository.findByNameAndDomain("Hidráulica", Domain.CONSTRUCTION).orElseThrow();
      Category eletrica = categoryRepository.findByNameAndDomain("Elétrica", Domain.CONSTRUCTION).orElseThrow();
      Category ferramentas = categoryRepository.findByNameAndDomain("Ferramentas", Domain.CONSTRUCTION).orElseThrow();
      Category seguranca = categoryRepository.findByNameAndDomain("Segurança", Domain.CONSTRUCTION).orElseThrow();

      // ACABAMENTO (20 produtos) - Todos como ConstructionProduct
      constructionProductRepository.saveAll(Arrays.asList(
          new ConstructionProduct("Tinta Látex Branca 18L", "Suvinil", 18.0, UnitType.L, 50, 89.99, acabamento, true,
              false, "Tinta látex PVA", "Tinta", "Interior", "Premium"),
          new ConstructionProduct("Tinta Látex Palha 18L", "Coral", 18.0, UnitType.L, 40, 92.99, acabamento, true,
              false, "Tinta látex acrílica", "Tinta", "Interior", "Premium"),
          new ConstructionProduct("Verniz Marítimo 3,6L", "Suvinil", 3.6, UnitType.L, 30, 45.99, acabamento, true,
              false, "Verniz poliuretano", "Verniz", "Exterior", "Profissional"),
          new ConstructionProduct("Primer Acrílico 18L", "Coral", 18.0, UnitType.L, 25, 79.99, acabamento, true, false,
              "Primer acrílico", "Primer", "Interior", "Standard"),
          new ConstructionProduct("Tinta Esmalte Branca 3,6L", "Sherwin-Williams", 3.6, UnitType.L, 35, 58.99,
              acabamento, true, false, "Esmalte sintético", "Tinta", "Exterior", "Premium"),
          new ConstructionProduct("Massa Corrida 25kg", "Suvinil", 25.0, UnitType.KG, 60, 32.99, acabamento, true,
              false, "Massa acrílica", "Massa", "Interior", "Standard"),
          new ConstructionProduct("Selador Acrílico 18L", "Coral", 18.0, UnitType.L, 20, 65.99, acabamento, true, false,
              "Selador acrílico", "Selador", "Interior", "Premium"),
          new ConstructionProduct("Piso Cerâmico 45x45cm", "Portobello", 1.8, UnitType.UN, 200, 19.99, acabamento, true,
              false, "Cerâmica esmaltada", "Piso", "Interior", "Standard"),
          new ConstructionProduct("Piso Porcelanato 60x60cm", "Eliane", 3.6, UnitType.UN, 150, 49.99, acabamento, true,
              false, "Porcelanato polido", "Piso", "Interior", "Premium"),
          new ConstructionProduct("Azulejo Branco 20x30cm", "Cecrisa", 0.6, UnitType.UN, 300, 12.99, acabamento, true,
              false, "Azulejo esmaltado", "Revestimento", "Interior", "Standard"),
          new ConstructionProduct("Rejunte Cinza 1kg", "Quartzolit", 1.0, UnitType.KG, 100, 8.99, acabamento, true,
              false, "Rejunte flexível", "Rejunte", "Interior", "Standard"),
          new ConstructionProduct("Rodapé PVC Branco 7cm", "Eucatex", 2.4, UnitType.UN, 80, 15.99, acabamento, true,
              false, "Rodapé PVC", "Rodapé", "Interior", "Standard"),
          new ConstructionProduct("Rodapé Madeira 10cm", "Eucatex", 2.4, UnitType.UN, 60, 28.99, acabamento, true,
              false, "Rodapé MDF", "Rodapé", "Interior", "Premium"),
          new ConstructionProduct("Argamassa Colante 20kg", "Quartzolit", 20.0, UnitType.KG, 120, 18.99, acabamento,
              true, false, "Argamassa AC-I", "Argamassa", "Interior", "Standard"),
          new ConstructionProduct("Gesso em Pó 40kg", "Fortaleza", 40.0, UnitType.KG, 80, 16.99, acabamento, true,
              false, "Gesso beta", "Gesso", "Interior", "Standard"),
          new ConstructionProduct("Lixa para Parede 220", "3M", 1.0, UnitType.UN, 200, 3.99, acabamento, true, false,
              "Lixa d'água", "Lixa", "Interior", "Standard"),
          new ConstructionProduct("Pincel 3 Polegadas", "Tigre", 1.0, UnitType.UN, 150, 12.99, acabamento, true, false,
              "Pincel cerdas naturais", "Ferramenta", "Geral", "Standard"),
          new ConstructionProduct("Rolo de Lã 23cm", "Tigre", 1.0, UnitType.UN, 100, 8.99, acabamento, true, false,
              "Rolo lã carneiro", "Ferramenta", "Geral", "Standard"),
          new ConstructionProduct("Fita Crepe 50mm", "3M", 1.0, UnitType.UN, 80, 7.99, acabamento, true, false,
              "Fita crepe papel", "Fita", "Geral", "Standard"),
          new ConstructionProduct("Solvente Thinner 1L", "Natrielli", 1.0, UnitType.L, 60, 9.99, acabamento, true,
              false, "Thinner comum", "Solvente", "Geral", "Standard")));

      // ESTRUTURAL (15 produtos)
      constructionProductRepository.saveAll(Arrays.asList(
          new ConstructionProduct("Cimento Portland 50kg", "Votorantim", 50.0, UnitType.KG, 100, 24.99, estrutural,
              true, false, "Cimento CP-II", "Cimento", "Estrutural", "Standard"),
          new ConstructionProduct("Cimento CP-II 50kg", "LafargeHolcim", 50.0, UnitType.KG, 80, 26.99, estrutural, true,
              false, "Cimento composto", "Cimento", "Estrutural", "Standard"),
          new ConstructionProduct("Ferro 8mm 12m", "Gerdau", 12.0, UnitType.UN, 120, 28.99, estrutural, true, false,
              "Vergalhão CA-50", "Ferro", "Estrutural", "Standard"),
          new ConstructionProduct("Ferro 10mm 12m", "ArcelorMittal", 12.0, UnitType.UN, 80, 45.99, estrutural, true,
              false, "Vergalhão CA-50", "Ferro", "Estrutural", "Standard"),
          new ConstructionProduct("Ferro 12mm 12m", "Gerdau", 12.0, UnitType.UN, 60, 65.99, estrutural, true, false,
              "Vergalhão CA-50", "Ferro", "Estrutural", "Standard"),
          new ConstructionProduct("Areia Média 1m³", "Mineração", 1000.0, UnitType.KG, 40, 45.00, estrutural, true,
              false, "Areia lavada", "Agregado", "Estrutural", "Standard"),
          new ConstructionProduct("Brita 1 - 1m³", "Mineração", 1000.0, UnitType.KG, 35, 55.00, estrutural, true, false,
              "Brita graduada", "Agregado", "Estrutural", "Standard"),
          new ConstructionProduct("Bloco de Concreto 14x19x39", "Tatu", 1.0, UnitType.UN, 500, 2.99, estrutural, true,
              false, "Bloco estrutural", "Bloco", "Estrutural", "Standard"),
          new ConstructionProduct("Tijolo 6 Furos", "Cerâmica", 1.0, UnitType.UN, 1000, 0.65, estrutural, true, false,
              "Tijolo cerâmico", "Tijolo", "Estrutural", "Standard"),
          new ConstructionProduct("Viga H 150x75mm", "Gerdau", 6.0, UnitType.UN, 20, 189.99, estrutural, true, false,
              "Viga metálica", "Viga", "Estrutural", "Premium"),
          new ConstructionProduct("Laje Treliçada EPS 12cm", "Lajes Fácil", 1.2, UnitType.UN, 100, 32.99, estrutural,
              true, false, "Laje pré-moldada", "Laje", "Estrutural", "Standard"),
          new ConstructionProduct("Vergalhão CA-50 6mm", "ArcelorMittal", 12.0, UnitType.UN, 200, 15.99, estrutural,
              true, false, "Vergalhão liso", "Ferro", "Estrutural", "Standard"),
          new ConstructionProduct("Concreto Usinado FCK 20", "Concreteira", 1000.0, UnitType.L, 50, 280.00, estrutural,
              true, false, "Concreto bombeável", "Concreto", "Estrutural", "Premium"),
          new ConstructionProduct("Arame Recozido 18", "Gerdau", 1.0, UnitType.KG, 100, 8.99, estrutural, true, false,
              "Arame mole", "Arame", "Estrutural", "Standard"),
          new ConstructionProduct("Cal Hidratada 20kg", "Supercal", 20.0, UnitType.KG, 150, 12.99, estrutural, true,
              false, "Cal CH-I", "Cal", "Estrutural", "Standard")));

      // HIDRÁULICA (18 produtos)
      constructionProductRepository.saveAll(Arrays.asList(
          new ConstructionProduct("Tubo PVC 100mm 6m", "Tigre", 6.0, UnitType.UN, 60, 32.99, hidraulica, true, false,
              "Tubo esgoto", "Tubo", "Hidráulica", "Standard"),
          new ConstructionProduct("Tubo PVC 50mm 6m", "Tigre", 6.0, UnitType.UN, 80, 18.99, hidraulica, true, false,
              "Tubo esgoto", "Tubo", "Hidráulica", "Standard"),
          new ConstructionProduct("Tubo PVC 25mm 6m", "Amanco", 6.0, UnitType.UN, 100, 12.99, hidraulica, true, false,
              "Tubo água fria", "Tubo", "Hidráulica", "Standard"),
          new ConstructionProduct("Joelho PVC 90° 100mm", "Tigre", 1.0, UnitType.UN, 50, 8.99, hidraulica, true, false,
              "Conexão esgoto", "Conexão", "Hidráulica", "Standard"),
          new ConstructionProduct("Joelho PVC 90° 50mm", "Amanco", 1.0, UnitType.UN, 80, 4.99, hidraulica, true, false,
              "Conexão esgoto", "Conexão", "Hidráulica", "Standard"),
          new ConstructionProduct("Tê PVC 100mm", "Tigre", 1.0, UnitType.UN, 40, 12.99, hidraulica, true, false,
              "Conexão esgoto", "Conexão", "Hidráulica", "Standard"),
          new ConstructionProduct("Cola PVC 175g", "Tigre", 175.0, UnitType.G, 100, 8.99, hidraulica, true, false,
              "Adesivo PVC", "Cola", "Hidráulica", "Standard"),
          new ConstructionProduct("Torneira Monocomando", "Deca", 1.0, UnitType.UN, 25, 129.99, hidraulica, true, false,
              "Torneira mesa", "Torneira", "Hidráulica", "Premium"),
          new ConstructionProduct("Torneira Jardim 1/2\"", "Docol", 1.0, UnitType.UN, 40, 25.99, hidraulica, true,
              false, "Torneira parede", "Torneira", "Hidráulica", "Standard"),
          new ConstructionProduct("Registro Gaveta 3/4\"", "Deca", 1.0, UnitType.UN, 30, 35.99, hidraulica, true, false,
              "Registro bruto", "Registro", "Hidráulica", "Standard"),
          new ConstructionProduct("Sifão Articulado", "Tigre", 1.0, UnitType.UN, 60, 15.99, hidraulica, true, false,
              "Sifão flexível", "Sifão", "Hidráulica", "Standard"),
          new ConstructionProduct("Vaso Sanitário Convencional", "Deca", 1.0, UnitType.UN, 15, 189.99, hidraulica, true,
              false, "Vaso com caixa", "Vaso", "Hidráulica", "Standard"),
          new ConstructionProduct("Pia Inox 1 Cuba", "Tramontina", 1.0, UnitType.UN, 20, 149.99, hidraulica, true,
              false, "Pia sobrepor", "Pia", "Hidráulica", "Standard"),
          new ConstructionProduct("Chuveiro Eletrônico", "Lorenzetti", 1.0, UnitType.UN, 25, 179.99, hidraulica, true,
              false, "Chuveiro 220V", "Chuveiro", "Hidráulica", "Premium"),
          new ConstructionProduct("Mangueira Jardim 15m", "Tramontina", 15.0, UnitType.UN, 35, 45.99, hidraulica, true,
              false, "Mangueira cristal", "Mangueira", "Hidráulica", "Standard"),
          new ConstructionProduct("Caixa D'água 1000L", "Fortlev", 1000.0, UnitType.L, 10, 299.99, hidraulica, true,
              false, "Caixa polietileno", "Caixa", "Hidráulica", "Standard"),
          new ConstructionProduct("Vedante Rosca 18mm", "Tigre", 1.0, UnitType.UN, 150, 3.99, hidraulica, true, false,
              "Fita veda rosca", "Vedante", "Hidráulica", "Standard"),
          new ConstructionProduct("Abraçadeira PVC 100mm", "Tigre", 1.0, UnitType.UN, 80, 5.99, hidraulica, true, false,
              "Abraçadeira esgoto", "Abraçadeira", "Hidráulica", "Standard")));

      // ELÉTRICA (15 produtos)
      constructionProductRepository.saveAll(Arrays.asList(
          new ConstructionProduct("Fio Elétrico 2,5mm² 100m", "Cobrecom", 100.0, UnitType.UN, 40, 89.99, eletrica, true,
              false, "Fio rígido", "Fio", "Elétrica", "Standard"),
          new ConstructionProduct("Fio Elétrico 1,5mm² 100m", "Pirelli", 100.0, UnitType.UN, 60, 55.99, eletrica, true,
              false, "Fio rígido", "Fio", "Elétrica", "Standard"),
          new ConstructionProduct("Cabo Flexível 4mm² 100m", "Cobrecom", 100.0, UnitType.UN, 30, 159.99, eletrica, true,
              false, "Cabo flexível", "Cabo", "Elétrica", "Premium"),
          new ConstructionProduct("Eletroduto PVC 25mm 3m", "Tigre", 3.0, UnitType.UN, 200, 8.99, eletrica, true, false,
              "Eletroduto rígido", "Eletroduto", "Elétrica", "Standard"),
          new ConstructionProduct("Eletroduto PVC 32mm 3m", "Amanco", 3.0, UnitType.UN, 150, 12.99, eletrica, true,
              false, "Eletroduto rígido", "Eletroduto", "Elétrica", "Standard"),
          new ConstructionProduct("Tomada 2P+T 10A", "Tramontina", 1.0, UnitType.UN, 150, 8.99, eletrica, true, false,
              "Tomada padrão", "Tomada", "Elétrica", "Standard"),
          new ConstructionProduct("Interruptor Simples", "Tramontina", 1.0, UnitType.UN, 200, 6.99, eletrica, true,
              false, "Interruptor 1 tecla", "Interruptor", "Elétrica", "Standard"),
          new ConstructionProduct("Disjuntor 20A", "Schneider", 1.0, UnitType.UN, 80, 15.99, eletrica, true, false,
              "Disjuntor curva C", "Disjuntor", "Elétrica", "Premium"),
          new ConstructionProduct("Quadro de Distribuição 12 Disjuntores", "Schneider", 1.0, UnitType.UN, 25, 89.99,
              eletrica, true, false, "Quadro embutir", "Quadro", "Elétrica", "Premium"),
          new ConstructionProduct("Lâmpada LED 12W", "Philips", 1.0, UnitType.UN, 100, 12.99, eletrica, true, false,
              "LED bulbo E27", "Lâmpada", "Elétrica", "Premium"),
          new ConstructionProduct("Lustres Pendente", "Startec", 1.0, UnitType.UN, 40, 45.99, eletrica, true, false,
              "Lustre decorativo", "Lustre", "Elétrica", "Premium"),
          new ConstructionProduct("Fita Isolante 10m", "3M", 10.0, UnitType.UN, 150, 4.99, eletrica, true, false,
              "Fita PVC", "Fita", "Elétrica", "Standard"),
          new ConstructionProduct("Conectores Isolados (100x)", "3M", 100.0, UnitType.UN, 80, 19.99, eletrica, true,
              false, "Conectores torção", "Conector", "Elétrica", "Standard"),
          new ConstructionProduct("Luminária Sobrepor LED", "Startec", 1.0, UnitType.UN, 60, 29.99, eletrica, true,
              false, "Luminária 18W", "Luminária", "Elétrica", "Premium"),
          new ConstructionProduct("Extensão 10m 3 Tomadas", "Tramontina", 10.0, UnitType.UN, 50, 35.99, eletrica, true,
              false, "Extensão elétrica", "Extensão", "Elétrica", "Standard")));

      // FERRAMENTAS (20 produtos)
      constructionProductRepository.saveAll(Arrays.asList(
          new ConstructionProduct("Furadeira de Impacto", "Bosch", 1.0, UnitType.UN, 12, 299.99, ferramentas, true,
              false, "Furadeira 650W", "Furadeira", "Ferramentas", "Premium"),
          new ConstructionProduct("Parafusadeira sem Fio", "Makita", 1.0, UnitType.UN, 15, 249.99, ferramentas, true,
              false, "Parafusadeira 12V", "Parafusadeira", "Ferramentas", "Premium"),
          new ConstructionProduct("Martelo 500g", "Tramontina", 0.5, UnitType.KG, 30, 24.99, ferramentas, true, false,
              "Martelo unha", "Martelo", "Ferramentas", "Standard"),
          new ConstructionProduct("Martelo de Borracha", "Gedore", 0.3, UnitType.KG, 25, 18.99, ferramentas, true,
              false, "Martelo borracha", "Martelo", "Ferramentas", "Standard"),
          new ConstructionProduct("Chave de Fenda 6\"", "Tramontina", 1.0, UnitType.UN, 80, 8.99, ferramentas, true,
              false, "Chave fenda", "Chave", "Ferramentas", "Standard"),
          new ConstructionProduct("Chave Phillips 6\"", "Tramontina", 1.0, UnitType.UN, 80, 8.99, ferramentas, true,
              false, "Chave phillips", "Chave", "Ferramentas", "Standard"),
          new ConstructionProduct("Alicate Universal 8\"", "Gedore", 1.0, UnitType.UN, 40, 35.99, ferramentas, true,
              false, "Alicate universal", "Alicate", "Ferramentas", "Premium"),
          new ConstructionProduct("Trena 5m", "Tramontina", 5.0, UnitType.UN, 60, 19.99, ferramentas, true, false,
              "Trena métrica", "Trena", "Ferramentas", "Standard"),
          new ConstructionProduct("Nível de Bolha 60cm", "Irwin", 0.6, UnitType.UN, 30, 45.99, ferramentas, true, false,
              "Nível alumínio", "Nível", "Ferramentas", "Premium"),
          new ConstructionProduct("Serra Circular 7.1/4\"", "Makita", 1.0, UnitType.UN, 8, 459.99, ferramentas, true,
              false, "Serra circular 1800W", "Serra", "Ferramentas", "Premium"),
          new ConstructionProduct("Esmerilhadeira 4.1/2\"", "Bosch", 1.0, UnitType.UN, 10, 179.99, ferramentas, true,
              false, "Esmerilhadeira 850W", "Esmerilhadeira", "Ferramentas", "Premium"),
          new ConstructionProduct("Chave Inglesa 12\"", "Gedore", 1.0, UnitType.UN, 25, 42.99, ferramentas, true, false,
              "Chave inglesa", "Chave", "Ferramentas", "Premium"),
          new ConstructionProduct("Esquadro 25cm", "Tramontina", 1.0, UnitType.UN, 50, 15.99, ferramentas, true, false,
              "Esquadro alumínio", "Esquadro", "Ferramentas", "Standard"),
          new ConstructionProduct("Lima Bastarda 8\"", "Pferd", 1.0, UnitType.UN, 40, 12.99, ferramentas, true, false,
              "Lima bastarda", "Lima", "Ferramentas", "Standard"),
          new ConstructionProduct("Serrote 20\"", "Tramontina", 1.0, UnitType.UN, 35, 28.99, ferramentas, true, false,
              "Serrote madeira", "Serrote", "Ferramentas", "Standard"),
          new ConstructionProduct("Talhadeira 1/2\"", "Gedore", 1.0, UnitType.UN, 60, 16.99, ferramentas, true, false,
              "Talhadeira aço", "Talhadeira", "Ferramentas", "Standard"),
          new ConstructionProduct("Ponteiro 1/2\"", "Gedore", 1.0, UnitType.UN, 60, 14.99, ferramentas, true, false,
              "Ponteiro aço", "Ponteiro", "Ferramentas", "Standard"),
          new ConstructionProduct("Morsa 4\"", "Gedore", 1.0, UnitType.UN, 15, 89.99, ferramentas, true, false,
              "Morsa bancada", "Morsa", "Ferramentas", "Premium"),
          new ConstructionProduct("Bancada de Trabalho", "Tramontina", 1.0, UnitType.UN, 8, 199.99, ferramentas, true,
              false, "Bancada madeira", "Bancada", "Ferramentas", "Premium"),
          new ConstructionProduct("Caixa de Ferramentas", "Tramontina", 1.0, UnitType.UN, 20, 59.99, ferramentas, true,
              false, "Caixa plástica", "Caixa", "Ferramentas", "Standard")));

      // SEGURANÇA (10 produtos)
      constructionProductRepository.saveAll(Arrays.asList(
          new ConstructionProduct("Capacete de Segurança", "3M", 1.0, UnitType.UN, 50, 25.99, seguranca, true, false,
              "Capacete classe A", "EPI", "Segurança", "Premium"),
          new ConstructionProduct("Óculos de Proteção", "3M", 1.0, UnitType.UN, 80, 12.99, seguranca, true, false,
              "Óculos anti-impacto", "EPI", "Segurança", "Premium"),
          new ConstructionProduct("Luva de Raspa", "CA", 1.0, UnitType.UN, 100, 8.99, seguranca, true, false,
              "Luva couro", "EPI", "Segurança", "Standard"),
          new ConstructionProduct("Bota de Segurança", "Marluvas", 1.0, UnitType.UN, 30, 89.99, seguranca, true, false,
              "Bota bico PVC", "EPI", "Segurança", "Premium"),
          new ConstructionProduct("Cinto de Segurança", "Alttura", 1.0, UnitType.UN, 20, 45.99, seguranca, true, false,
              "Cinto paraquedista", "EPI", "Segurança", "Premium"),
          new ConstructionProduct("Protetor Auricular", "3M", 1.0, UnitType.UN, 60, 5.99, seguranca, true, false,
              "Protetor silicone", "EPI", "Segurança", "Standard"),
          new ConstructionProduct("Máscara PFF2", "3M", 1.0, UnitType.UN, 200, 3.99, seguranca, true, false,
              "Máscara descartável", "EPI", "Segurança", "Standard"),
          new ConstructionProduct("Colete Refletivo", "Vonder", 1.0, UnitType.UN, 40, 18.99, seguranca, true, false,
              "Colete alta visibilidade", "EPI", "Segurança", "Standard"),
          new ConstructionProduct("Extintor de Incêndio 4kg", "Bucka", 4.0, UnitType.KG, 15, 89.99, seguranca, true,
              false, "Extintor ABC", "Segurança", "Segurança", "Premium"),
          new ConstructionProduct("Placa de Sinalização", "Sinalize", 1.0, UnitType.UN, 50, 12.99, seguranca, true,
              false, "Placa PVC", "Sinalização", "Segurança", "Standard")));

      System.out.println("✅ Produtos de construção inseridos! (98+ itens)");
    }
  }

  /**
   * Seed das promoções específicas por domínio (desabilitado)
   * TODO: Implementar promoções específicas para cada domínio se necessário
   */
  private void seedPromotions() {
    System.out.println("🔵 Promoções desabilitadas - apenas produtos específicos por domínio.");
  }
}
