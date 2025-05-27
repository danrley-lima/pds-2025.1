package com.danrley.product_management.config;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.danrley.product_management.model.category.Category;
import com.danrley.product_management.model.product.Product;
import com.danrley.product_management.model.product.UnitType;
import com.danrley.product_management.model.promotion.Promotion;
import com.danrley.product_management.repository.CategoryRepository;
import com.danrley.product_management.repository.ProductRepository;
import com.danrley.product_management.repository.PromotionRepository;

import lombok.RequiredArgsConstructor;

@Component
@Profile({ "dev", "test" })
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

  @Value("${app.database-seeder-enabled:false}")
  private boolean isSeederEnabled;

  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;
  private final PromotionRepository promotionRepository;

  @Override
  public void run(String... args) throws Exception {
    if (!isSeederEnabled) {
      return;
    }

    if (categoryRepository.count() == 0) {
      List<Category> categories = Arrays.asList(
          new Category("Carnes"),
          new Category("Hortifruti"),
          new Category("Temperos"),
          new Category("Enlatados"),
          new Category("Laticínios"),
          new Category("Bebidas"),
          new Category("Padaria"),
          new Category("Produtos de limpeza"),
          new Category("Grãos e Cereais"),
          new Category("Óleos e Gorduras"),
          new Category("Massas"),
          new Category("Doces e Sobremesas"));

      categoryRepository.saveAll(categories);
      System.out.println("🟢 Categorias inseridas com sucesso!");
    }

    if (productRepository.count() == 0) {
      Category carnes = categoryRepository.findByName("Carnes").orElseThrow();
      Category hortifruti = categoryRepository.findByName("Hortifruti").orElseThrow();
      Category temperos = categoryRepository.findByName("Temperos").orElseThrow();
      Category enlatados = categoryRepository.findByName("Enlatados").orElseThrow();
      Category laticinios = categoryRepository.findByName("Laticínios").orElseThrow();
      Category bebidas = categoryRepository.findByName("Bebidas").orElseThrow();
      Category padaria = categoryRepository.findByName("Padaria").orElseThrow();
      Category limpeza = categoryRepository.findByName("Produtos de limpeza").orElseThrow();
      Category graos = categoryRepository.findByName("Grãos e Cereais").orElseThrow();
      Category oleos = categoryRepository.findByName("Óleos e Gorduras").orElseThrow();
      Category massas = categoryRepository.findByName("Massas").orElseThrow();
      Category doces = categoryRepository.findByName("Doces e Sobremesas").orElseThrow();

      productRepository.saveAll(Arrays.asList(
          new Product("Frango em Cubos", "Sadia", 500.0, UnitType.G, 50, 18.0, carnes, true, false),
          new Product("Carne Moída", "Friboi", 1000.0, UnitType.G, 100, 22.0, carnes, true, false),
          new Product("Costela de Boi", "Seara", 1000.0, UnitType.G, 30, 35.0, carnes, true, false),
          new Product("Linguiça Toscana", "Perdigão", 800.0, UnitType.G, 40, 16.0, carnes, true, false),
          new Product("Peito de Frango", "Sadia", 1000.0, UnitType.G, 60, 20.0, carnes, true, false),
          new Product("Ovo Branco", "Granja Feliz", 60.0, UnitType.UN, 360, 0.80, carnes, true, false),

          new Product("Cebola", "Natural", 100.0, UnitType.G, 200, 3.5, hortifruti, true, false),
          new Product("Tomate", "Agrícola", 200.0, UnitType.G, 150, 4.0, hortifruti, true, false),
          new Product("Alface", "Orgânica", 300.0, UnitType.G, 70, 5.0, hortifruti, true, false),
          new Product("Batata", "Natural", 500.0, UnitType.G, 180, 4.2, hortifruti, true, false),
          new Product("Cenoura", "Natural", 200.0, UnitType.G, 120, 3.8, hortifruti, true, false),
          new Product("Abobrinha", "Natural", 300.0, UnitType.G, 60, 4.0, hortifruti, true, false),
          new Product("Repolho", "Natural", 500.0, UnitType.G, 40, 3.5, hortifruti, true, false),
          new Product("Banana", "Prata", 1000.0, UnitType.G, 100, 6.0, hortifruti, true, false),
          new Product("Maçã", "Fuji", 1000.0, UnitType.G, 80, 7.0, hortifruti, true, false),

          new Product("Alho", "Natural", 10.0, UnitType.G, 100, 2.0, temperos, true, false),
          new Product("Sal", "Refinado", 1000.0, UnitType.G, 500, 1.5, temperos, true, false),
          new Product("Pimenta do Reino", "Sadia", 50.0, UnitType.G, 80, 5.0, temperos, true, false),
          new Product("Orégano", "Kitano", 20.0, UnitType.G, 60, 2.5, temperos, true, false),
          new Product("Colorau", "Natural", 50.0, UnitType.G, 70, 2.2, temperos, true, false),
          new Product("Salsinha", "Natural", 30.0, UnitType.G, 50, 2.0, temperos, true, false),
          new Product("Cebolinha", "Natural", 30.0, UnitType.G, 50, 2.0, temperos, true, false),
          new Product("Louro", "Kitano", 5.0, UnitType.G, 30, 1.5, temperos, true, false),

          new Product("Extrato de Tomate", "Elefante", 130.0, UnitType.G, 100, 3.8, enlatados, true, false),
          new Product("Sardinha", "Pescada", 180.0, UnitType.G, 60, 4.5, enlatados, true, false),
          new Product("Milho Verde", "Quero", 200.0, UnitType.G, 90, 3.0, enlatados, true, false),
          new Product("Ervilha", "Quero", 200.0, UnitType.G, 80, 3.2, enlatados, true, false),
          new Product("Atum", "Gomes da Costa", 170.0, UnitType.G, 70, 6.0, enlatados, true, false),
          new Product("Feijão Preto Enlatado", "Camil", 400.0, UnitType.G, 50, 5.0, enlatados, true, false),

          new Product("Leite", "Italac", 1000.0, UnitType.ML, 200, 4.0, laticinios, true, false),
          new Product("Queijo Mussarela", "Vigor", 300.0, UnitType.G, 120, 15.0, laticinios, true, false),
          new Product("Manteiga", "Nestlé", 200.0, UnitType.G, 80, 5.0, laticinios, true, false),
          new Product("Iogurte Natural", "Danone", 170.0, UnitType.G, 60, 2.8, laticinios, true, false),
          new Product("Requeijão", "Polenghi", 200.0, UnitType.G, 50, 6.5, laticinios, true, false),
          new Product("Leite Condensado", "Moça", 395.0, UnitType.G, 100, 7.0, laticinios, true, false),
          new Product("Creme de Leite", "Nestlé", 200.0, UnitType.G, 100, 4.5, laticinios, true, false),

          new Product("Coca-Cola", "Coca-Cola", 500.0, UnitType.ML, 250, 6.0, bebidas, true, false),
          new Product("Suco de Laranja", "Del Valle", 1000.0, UnitType.ML, 100, 8.0, bebidas, true, false),
          new Product("Água Mineral", "Crystal", 500.0, UnitType.ML, 300, 2.0, bebidas, true, false),
          new Product("Cerveja", "Skol", 350.0, UnitType.ML, 200, 3.5, bebidas, true, false),
          new Product("Café", "Pilão", 500.0, UnitType.G, 100, 12.0, bebidas, true, false),
          new Product("Chá Mate", "Leão", 250.0, UnitType.G, 80, 6.0, bebidas, true, false),

          new Product("Pão Francês", "Padaria do Povo", 50.0, UnitType.G, 200, 1.0, padaria, true, false),
          new Product("Baguete", "Padaria Pão Quente", 200.0, UnitType.G, 150, 2.5, padaria, true, false),
          new Product("Croissant", "Padaria Central", 80.0, UnitType.G, 60, 3.0, padaria, true, false),
          new Product("Pão de Forma", "Wickbold", 400.0, UnitType.G, 90, 7.0, padaria, true, false),
          new Product("Bolo de Fubá", "Padaria Central", 500.0, UnitType.G, 40, 10.0, padaria, true, false),

          new Product("Sabão em Pó", "Omo", 500.0, UnitType.G, 80, 15.0, limpeza, true, false),
          new Product("Desinfetante", "Veja", 1000.0, UnitType.ML, 60, 8.0, limpeza, true, false),
          new Product("Água Sanitária", "Qboa", 1000.0, UnitType.ML, 70, 4.0, limpeza, true, false),
          new Product("Detergente", "Ypê", 500.0, UnitType.ML, 100, 2.5, limpeza, true, false),

          new Product("Arroz Branco", "Camil", 1000.0, UnitType.G, 300, 6.0, graos, true, false),
          new Product("Feijão Carioca", "Camil", 1000.0, UnitType.G, 250, 8.0, graos, true, false),
          new Product("Farinha de Mandioca", "Yoki", 500.0, UnitType.G, 100, 5.0, graos, true, false),
          new Product("Açúcar Refinado", "União", 1000.0, UnitType.G, 200, 4.0, graos, true, false),
          new Product("Aveia em Flocos", "Quaker", 200.0, UnitType.G, 80, 4.5, graos, true, false),
          new Product("Milho para Pipoca", "Yoki", 500.0, UnitType.G, 60, 3.0, graos, true, false),

          new Product("Óleo de Soja", "Soya", 900.0, UnitType.ML, 150, 7.0, oleos, true, false),
          new Product("Azeite de Oliva", "Andorinha", 500.0, UnitType.ML, 60, 22.0, oleos, true, false),
          new Product("Margarina", "Qualy", 500.0, UnitType.G, 80, 6.0, oleos, true, false),

          new Product("Macarrão Espaguete", "Renata", 500.0, UnitType.G, 120, 4.0, massas, true, false),
          new Product("Macarrão Parafuso", "Renata", 500.0, UnitType.G, 100, 4.0, massas, true, false),
          new Product("Lasanha", "Adria", 500.0, UnitType.G, 50, 8.0, massas, true, false),

          new Product("Chocolate ao Leite", "Nestlé", 90.0, UnitType.G, 100, 5.0, doces, true, false),
          new Product("Goiabada", "Predilecta", 300.0, UnitType.G, 60, 4.0, doces, true, false),
          new Product("Biscoito Maizena", "Nestlé", 200.0, UnitType.G, 120, 3.5, doces, true, false)));

      System.out.println("🟢 Produtos inseridos com sucesso!");
    } else {
      System.out.println("🔵 Produtos já existem. Seed ignorado.");
    }

    if (promotionRepository.count() == 0) {
      Product product = productRepository.findByName("Frango em Cubos").orElseThrow();
      Product product2 = productRepository.findByName("Carne Moída").orElseThrow();
      Product product3 = productRepository.findByName("Cebola").orElseThrow();
      Product product4 = productRepository.findByName("Café").orElseThrow();

      // Calcular datas dinâmicas para as promoções
      LocalDate today = LocalDate.now();
      LocalDate startDate = today.minusDays(5);
      LocalDate endDate = today.plusDays(10);

      Promotion promotion1 = new Promotion();
      promotion1.setPromotionalPrice(5.99);
      promotion1.setStartDate(startDate);
      promotion1.setEndDate(endDate);
      promotion1.setDescription("Dia das carnes!!");
      promotion1.setProduct(product);
      promotionRepository.save(promotion1);
      System.out.println("Promoção criada para: " + product.getName());

      Promotion promotion2 = new Promotion();
      promotion2.setPromotionalPrice(15.99);
      promotion2.setStartDate(startDate);
      promotion2.setEndDate(endDate);
      promotion2.setDescription("Dia das carnes!!");
      promotion2.setProduct(product2);
      promotionRepository.save(promotion2);
      System.out.println("Promoção criada para: " + product2.getName());

      Promotion promotion3 = new Promotion();
      promotion3.setPromotionalPrice(2.99);
      promotion3.setStartDate(startDate);
      promotion3.setEndDate(endDate);
      promotion3.setDescription("Dia das verduras!");
      promotion3.setProduct(product3);
      promotionRepository.save(promotion3);
      System.out.println("Promoção criada para: " + product3.getName());

      Promotion promotion4 = new Promotion();
      promotion4.setPromotionalPrice(9.99);
      promotion4.setStartDate(startDate);
      promotion4.setEndDate(endDate);
      promotion4.setDescription("Oferta do dia!");
      promotion4.setProduct(product4);
      promotionRepository.save(promotion4);
      System.out.println("Promoção criada para: " + product4.getName());

      System.out.println("🟢 Promoções inseridas com sucesso!");
      System.out.println("📅 Período das promoções: " + startDate + " até " + endDate);
    }
  }
}
