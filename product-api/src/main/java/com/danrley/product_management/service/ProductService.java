package com.danrley.product_management.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danrley.product_management.dto.product.ProductRequestDTO;
import com.danrley.product_management.dto.product.ProductResponseDTO;
import com.danrley.product_management.exception.custom.CategoryNotFoundException;
import com.danrley.product_management.exception.custom.ProductNotFoundException;
import com.danrley.product_management.exception.custom.ProductServiceException;
import com.danrley.product_management.exception.custom.ProductValidationException;
import com.danrley.product_management.model.category.Category;
import com.danrley.product_management.model.product.Product;
import com.danrley.product_management.repository.CategoryRepository;
import com.danrley.product_management.repository.ProductRepository;

@Service
public class ProductService {

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private ProductRepository productRepository;

  public List<ProductResponseDTO> getAll() {
    try {
      return productRepository.findAll()
          .stream()
          .map(this::toResponse)
          .collect(Collectors.toList());
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produtos: " + e.getMessage(), e);
    }
  }

  public ProductResponseDTO getById(Long id) {
    if (id == null) {
      throw new ProductValidationException("ID do produto não pode ser nulo");
    }

    try {
      return productRepository.findById(id)
          .map(this::toResponse)
          .orElseThrow(() -> new ProductNotFoundException(id));
    } catch (ProductNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produto com ID " + id + ": " + e.getMessage(), e);
    }
  }

  @Transactional
  public ProductResponseDTO create(ProductRequestDTO dto) {
    try {
      validateProductRequest(dto, true);

      Product product = new Product();
      mapRequestToProduct(dto, product);

      Product savedProduct = productRepository.save(product);
      return toResponse(savedProduct);
    } catch (DataIntegrityViolationException e) {
      throw new ProductValidationException(
          "Produto com dados duplicados. Verifique se já existe um produto com mesmo nome.", e);
    } catch (ProductValidationException | CategoryNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao criar produto: " + e.getMessage(), e);
    }
  }

  @Transactional
  public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
    if (id == null) {
      throw new ProductValidationException("ID do produto não pode ser nulo");
    }

    try {
      validateProductRequest(dto, false);

      Product product = productRepository.findById(id)
          .orElseThrow(() -> new ProductNotFoundException(id));

      mapRequestToProduct(dto, product);
      Product updatedProduct = productRepository.save(product);
      return toResponse(updatedProduct);
    } catch (ProductNotFoundException | ProductValidationException | CategoryNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao atualizar produto com ID " + id + ": " + e.getMessage(), e);
    }
  }

  @Transactional
  public void delete(Long id) {
    if (id == null) {
      throw new ProductValidationException("ID do produto não pode ser nulo");
    }

    try {
      if (!productRepository.existsById(id)) {
        throw new ProductNotFoundException(id);
      }

      productRepository.deleteById(id);
    } catch (ProductNotFoundException e) {
      throw e;
    } catch (DataIntegrityViolationException e) {
      throw new ProductValidationException("Não é possível excluir o produto. Existem registros associados a ele.", e);
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao excluir produto com ID " + id + ": " + e.getMessage(), e);
    }
  }

  private void validateProductRequest(ProductRequestDTO dto, boolean isCreating) {
    Map<String, String> validationErrors = new HashMap<>();

    if (isCreating) {
      if (dto.name == null || dto.name.trim().isEmpty()) {
        validationErrors.put("name", "Nome do produto é obrigatório");
      }

      if (dto.categoryId == null) {
        validationErrors.put("categoryId", "Categoria do produto é obrigatória");
      }

      if (dto.unitPrice == null) {
        validationErrors.put("unitPrice", "Preço do produto é obrigatório");
      }

      if (dto.priority == null) {
        validationErrors.put("priority", "Prioridade é obrigatória");
      }
    }

    if (dto.unitPrice != null && dto.unitPrice <= 0) {
      validationErrors.put("unitPrice", "Preço deve ser maior que zero");
    }

    if (dto.unitWeight != null && dto.unitWeight <= 0) {
      validationErrors.put("unitWeight", "Peso deve ser maior que zero");
    }

    if (dto.stockQuantity != null && dto.stockQuantity < 0) {
      validationErrors.put("stockQuantity", "Quantidade em estoque não pode ser negativa");
    }

    if (!validationErrors.isEmpty()) {
      throw new ProductValidationException("Erro de validação nos dados do produto", validationErrors);
    }
  }

  private void mapRequestToProduct(ProductRequestDTO dto, Product product) {
    if (dto.name != null)
      product.setName(dto.name);
    if (dto.brand != null)
      product.setBrand(dto.brand);
    if (dto.unitWeight != null)
      product.setUnitWeight(dto.unitWeight);
    if (dto.unitType != null)
      product.setUnitType(dto.unitType);
    if (dto.stockQuantity != null)
      product.setStockQuantity(dto.stockQuantity);
    if (dto.unitPrice != null)
      product.setUnitPrice(dto.unitPrice);
    product.setPriority(dto.priority);
    if (dto.categoryId != null) {
      Category category = categoryRepository.findById(dto.categoryId)
          .orElseThrow(() -> new CategoryNotFoundException(dto.categoryId));
      product.setCategory(category);
    }
  }

  private ProductResponseDTO toResponse(Product product) {
    ProductResponseDTO dto = new ProductResponseDTO();
    dto.id = product.getId();
    dto.name = product.getName();
    dto.brand = product.getBrand();
    dto.unitWeight = product.getUnitWeight();
    dto.unitType = product.getUnitType();
    dto.stockQuantity = product.getStockQuantity();
    dto.unitPrice = product.getUnitPrice();
    dto.available = product.isAvailable();
    dto.priority = product.isPriority();
    dto.categoryName = product.getCategory() != null ? product.getCategory().getName() : null;

    if (product.getPromotion() != null) {
      dto.onPromotion = true;
      dto.promotionalPrice = product.getPromotion().getPromotionalPrice();
    } else {
      dto.onPromotion = false;
    }

    return dto;
  }

  public List<ProductResponseDTO> getByIds(List<Long> ids) {
    if (ids == null || ids.isEmpty()) {
      throw new ProductValidationException("Lista de IDs não pode ser vazia");
    }

    try {
      List<Product> foundProducts = productRepository.findAllById(ids);

      // Verificar quais IDs não foram encontrados
      if (foundProducts.size() < ids.size()) {
        Set<Long> foundIds = foundProducts.stream()
            .map(Product::getId)
            .collect(Collectors.toSet());

        List<Long> missingIds = ids.stream()
            .filter(id -> !foundIds.contains(id))
            .collect(Collectors.toList());

        if (!missingIds.isEmpty()) {
          // Usar o construtor específico para lista de IDs
          throw new ProductNotFoundException(missingIds);
        }
      }

      return foundProducts.stream()
          .map(this::toResponse)
          .collect(Collectors.toList());
    } catch (ProductNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produtos por IDs: " + e.getMessage(), e);
    }
  }

  public List<ProductResponseDTO> getProductsOnPromotion() {
    try {
      LocalDate today = LocalDate.now();
      return productRepository.findProductsWithActivePromotions(today)
          .stream()
          .map(this::toResponse)
          .collect(Collectors.toList());
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produtos em promoção: " + e.getMessage(), e);
    }
  }
}
