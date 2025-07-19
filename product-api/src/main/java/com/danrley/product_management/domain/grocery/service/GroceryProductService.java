package com.danrley.product_management.domain.grocery.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danrley.product_management.common.dto.product.ProductRequestDTO;
import com.danrley.product_management.common.dto.product.ProductResponseDTO;
import com.danrley.product_management.common.exception.custom.CategoryNotFoundException;
import com.danrley.product_management.common.exception.custom.ProductNotFoundException;
import com.danrley.product_management.common.exception.custom.ProductServiceException;
import com.danrley.product_management.common.exception.custom.ProductValidationException;
import com.danrley.product_management.common.model.category.Category;
import com.danrley.product_management.common.repository.CategoryRepository;
import com.danrley.product_management.core.service.BaseProductService;
import com.danrley.product_management.domain.grocery.model.GroceryProduct;
import com.danrley.product_management.domain.grocery.repository.GroceryProductRepository;

/**
 * Serviço para produtos do supermercado.
 */
@Service
public class GroceryProductService implements BaseProductService<GroceryProduct> {

  @Autowired
  private GroceryProductRepository groceryProductRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  // Métodos base

  @Override
  public List<GroceryProduct> getAll() {
    try {
      return groceryProductRepository.findAll();
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produtos do supermercado: " + e.getMessage(), e);
    }
  }

  @Override
  public Optional<GroceryProduct> getById(Long id) {
    if (id == null) {
      throw new ProductValidationException("ID do produto não pode ser nulo");
    }

    try {
      return groceryProductRepository.findById(id);
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produto com ID " + id + ": " + e.getMessage(), e);
    }
  }

  @Override
  public List<GroceryProduct> getProductsByIds(List<Long> ids) {
    if (ids == null || ids.isEmpty()) {
      throw new ProductValidationException("Lista de IDs de produtos não pode ser nula ou vazia");
    }

    try {
      List<GroceryProduct> products = groceryProductRepository.findAllById(ids);

      // Garante que todos os IDs foram encontrados, se necessário
      if (products.size() != ids.size()) {
        List<Long> foundIds = products.stream()
            .map(GroceryProduct::getId)
            .toList();

        List<Long> missingIds = ids.stream()
            .filter(id -> !foundIds.contains(id))
            .toList();

        throw new ProductNotFoundException("Produtos não encontrados para os IDs: " + missingIds);
      }

      return products;
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produtos pelos IDs: " + e.getMessage(), e);
    }
  }

  @Override
  @Transactional
  public GroceryProduct create(ProductRequestDTO dto) {
    try {
      validateProductRequest(dto, true);

      GroceryProduct product = new GroceryProduct();
      mapRequestToProduct(dto, product);

      return groceryProductRepository.save(product);
    } catch (DataIntegrityViolationException e) {
      throw new ProductValidationException(
          "Produto com dados duplicados. Verifique se já existe um produto com mesmo nome.", e);
    } catch (ProductValidationException | CategoryNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao criar produto: " + e.getMessage(), e);
    }
  }

  @Override
  @Transactional
  public GroceryProduct update(Long id, ProductRequestDTO dto) {
    if (id == null) {
      throw new ProductValidationException("ID do produto não pode ser nulo");
    }

    try {
      validateProductRequest(dto, false);

      GroceryProduct product = groceryProductRepository.findById(id)
          .orElseThrow(() -> new ProductNotFoundException(id));

      mapRequestToProduct(dto, product);
      return groceryProductRepository.save(product);
    } catch (ProductNotFoundException | ProductValidationException | CategoryNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao atualizar produto: " + e.getMessage(), e);
    }
  }

  @Override
  @Transactional
  public void delete(Long id) {
    if (id == null) {
      throw new ProductValidationException("ID do produto não pode ser nulo");
    }

    try {
      GroceryProduct product = groceryProductRepository.findById(id)
          .orElseThrow(() -> new ProductNotFoundException(id));

      groceryProductRepository.delete(product);
    } catch (ProductNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao deletar produto: " + e.getMessage(), e);
    }
  }

  @Override
  public ProductResponseDTO toResponseDTO(GroceryProduct entity) {
    return ProductResponseDTO.fromGroceryProduct(entity);
  }

  @Override
  public List<GroceryProduct> getByCategory(String category) {
    try {
      // Buscar categoria pelo nome
      Category categoryEntity = categoryRepository.findByName(category)
          .orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada: " + category));
      return groceryProductRepository.findByCategory(categoryEntity);
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produtos por categoria: " + e.getMessage(), e);
    }
  }

  @Override
  public List<GroceryProduct> getByBrand(String brand) {
    try {
      return groceryProductRepository.findByBrand(brand);
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produtos por marca: " + e.getMessage(), e);
    }
  }

  @Override
  public List<GroceryProduct> getAvailable() {
    try {
      return groceryProductRepository.findAvailableProducts();
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produtos disponíveis: " + e.getMessage(), e);
    }
  }

  // Métodos do domínio

  /**
   * Busca produtos prioritários.
   */
  public List<GroceryProduct> getPriorityProducts() {
    try {
      return groceryProductRepository.findPriorityProducts();
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produtos prioritários: " + e.getMessage(), e);
    }
  }

  // /**
  // * Busca produtos com promoções ativas.
  // */
  // public List<GroceryProduct> getProductsWithActivePromotions() {
  // try {
  // LocalDate today = LocalDate.now();
  // return groceryProductRepository.findProductsWithActivePromotions(today);
  // } catch (Exception e) {
  // throw new ProductServiceException("Erro ao buscar produtos em promoção: " +
  // e.getMessage(), e);
  // }
  // }

  /**
   * Busca produtos por categoria.
   */
  public List<GroceryProduct> getByCategory(Category category) {
    try {
      return groceryProductRepository.findByCategory(category);
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produtos por categoria: " + e.getMessage(), e);
    }
  }

  // Métodos auxiliares

  private void validateProductRequest(ProductRequestDTO dto, boolean isCreate) {
    if (dto == null) {
      throw new ProductValidationException("Dados do produto não podem ser nulos");
    }

    if (dto.name == null || dto.name.trim().isEmpty()) {
      throw new ProductValidationException("Nome do produto é obrigatório");
    }

    if (dto.unitPrice == null || dto.unitPrice <= 0) {
      throw new ProductValidationException("Preço unitário deve ser maior que zero");
    }

    if (dto.stockQuantity == null || dto.stockQuantity < 0) {
      throw new ProductValidationException("Quantidade em estoque não pode ser negativa");
    }

    if (dto.categoryId == null) {
      throw new ProductValidationException("Categoria é obrigatória");
    }

    // Verificar se categoria existe
    categoryRepository.findById(dto.categoryId)
        .orElseThrow(() -> new CategoryNotFoundException(dto.categoryId));
  }

  private void mapRequestToProduct(ProductRequestDTO dto, GroceryProduct product) {
    product.setName(dto.name);
    product.setBrand(dto.brand);
    product.setUnitWeight(dto.unitWeight);

    if (dto.unitType != null) {
      product.setUnitType(dto.unitType);
    }

    product.setStockQuantity(dto.stockQuantity);
    product.setUnitPrice(dto.unitPrice);
    product.setAvailable(true); // Sempre disponível por padrão
    product.setPriority(dto.priority != null ? dto.priority : false);

    Category category = categoryRepository.findById(dto.categoryId)
        .orElseThrow(() -> new CategoryNotFoundException(dto.categoryId));
    product.setCategory(category);

    if (dto.expirationDate != null) {
      product.setExpirationDate(dto.expirationDate);
    }

  }
}
