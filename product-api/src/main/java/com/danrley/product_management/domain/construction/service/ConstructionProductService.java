package com.danrley.product_management.domain.construction.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danrley.product_management.common.dto.product.ProductRequestDTO;
import com.danrley.product_management.common.dto.product.ProductResponseDTO;
import com.danrley.product_management.common.dto.promotionCondition.PromotionConditionRequestDTO;
import com.danrley.product_management.common.exception.custom.CategoryNotFoundException;
import com.danrley.product_management.common.exception.custom.ProductNotFoundException;
import com.danrley.product_management.common.exception.custom.ProductServiceException;
import com.danrley.product_management.common.exception.custom.ProductValidationException;
import com.danrley.product_management.common.model.category.Category;
import com.danrley.product_management.common.repository.CategoryRepository;
import com.danrley.product_management.core.service.BaseProductService;
import com.danrley.product_management.domain.construction.model.ConstructionProduct;
import com.danrley.product_management.domain.construction.model.ConstructionPromotion;
import com.danrley.product_management.domain.construction.repository.ConstructionProductRepository;
import com.danrley.product_management.domain.construction.repository.ConstructionPromotionRepository;

/**
 * Serviço para produtos de construção.
 */
@Service
public class ConstructionProductService implements BaseProductService<ConstructionProduct> {

  @Autowired
  private ConstructionProductRepository constructionProductRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private ConstructionPromotionRepository constructionPromotionRepository;

  // Métodos base

  @Override
  public List<ConstructionProduct> getAll() {
    try {
      return constructionProductRepository.findAll();
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produtos de construção: " + e.getMessage(), e);
    }
  }

  @Override
  public Optional<ConstructionProduct> getById(Long id) {
    if (id == null) {
      throw new ProductValidationException("ID do produto não pode ser nulo");
    }

    try {
      return constructionProductRepository.findById(id);
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produto com ID " + id + ": " + e.getMessage(), e);
    }
  }

  @Override
  @Transactional
  public ConstructionProduct create(ProductRequestDTO dto) {
    try {
      validateProductRequest(dto, true);

      ConstructionProduct product = new ConstructionProduct();
      mapRequestToProduct(dto, product);

      return constructionProductRepository.save(product);
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
  public ConstructionProduct update(Long id, ProductRequestDTO dto) {
    if (id == null) {
      throw new ProductValidationException("ID do produto não pode ser nulo");
    }

    try {
      validateProductRequest(dto, false);

      ConstructionProduct product = constructionProductRepository.findById(id)
          .orElseThrow(() -> new ProductNotFoundException(id));

      mapRequestToProduct(dto, product);
      return constructionProductRepository.save(product);
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
      ConstructionProduct product = constructionProductRepository.findById(id)
          .orElseThrow(() -> new ProductNotFoundException(id));

      constructionProductRepository.delete(product);
    } catch (ProductNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao deletar produto: " + e.getMessage(), e);
    }
  }


  @Override
  public ProductResponseDTO toResponseDTO(ConstructionProduct entity) {
    return ProductResponseDTO.fromConstructionProduct(entity);
  }

  @Override
  public List<ConstructionProduct> getByCategory(String category) {
    try {
      // Buscar categoria pelo nome
      Category categoryEntity = categoryRepository.findByName(category)
          .orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada: " + category));
      return constructionProductRepository.findByCategory(categoryEntity);
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produtos por categoria: " + e.getMessage(), e);
    }
  }

  @Override
  public List<ConstructionProduct> getByBrand(String brand) {
    try {
      // Busca por marca usando filtro de stream
      return constructionProductRepository.findAll().stream()
          .filter(p -> p.getBrand() != null && p.getBrand().toLowerCase().contains(brand.toLowerCase()))
          .toList();
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produtos por marca: " + e.getMessage(), e);
    }
  }

  @Override
  public List<ConstructionProduct> getAvailable() {
    try {
      return constructionProductRepository.findByAvailableTrue();
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produtos disponíveis: " + e.getMessage(), e);
    }
  }

  // Métodos do domínio

  /**
   * Busca produtos por especificações.
   */
  public List<ConstructionProduct> getBySpecifications(String specifications) {
    try {
      return constructionProductRepository.findAll().stream()
          .filter(p -> p.getSpecifications() != null &&
              p.getSpecifications().toLowerCase().contains(specifications.toLowerCase()))
          .toList();
    } catch (Exception e) {
      throw new ProductServiceException("Erro ao buscar produtos por especificações: " + e.getMessage(), e);
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

  private void mapRequestToProduct(ProductRequestDTO dto, ConstructionProduct product) {
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

    // Buscar e definir categoria
    Category category = categoryRepository.findById(dto.categoryId)
        .orElseThrow(() -> new CategoryNotFoundException(dto.categoryId));
    product.setCategory(category);

    // Campos de construção
    if (dto.specifications != null) {
      product.setSpecifications(dto.specifications);
    }
  }

  @Override
  public List<ConstructionProduct> getProductsByIds(List<Long> ids) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getProductsByIds'");
  }
}
