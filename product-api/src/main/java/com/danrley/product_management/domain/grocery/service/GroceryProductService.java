package com.danrley.product_management.domain.grocery.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danrley.product_management.domain.grocery.model.GroceryProduct;
import com.danrley.product_management.domain.grocery.repository.GroceryProductRepository;
import com.danrley.product_management.dto.product.ProductRequestDTO;
import com.danrley.product_management.exception.custom.CategoryNotFoundException;
import com.danrley.product_management.exception.custom.ProductNotFoundException;
import com.danrley.product_management.exception.custom.ProductServiceException;
import com.danrley.product_management.exception.custom.ProductValidationException;
import com.danrley.product_management.model.category.Category;
import com.danrley.product_management.repository.CategoryRepository;

/**
 * Serviço específico para produtos do domínio grocery.
 */
@Service
public class GroceryProductService {

    @Autowired
    private GroceryProductRepository groceryProductRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<GroceryProduct> getAll() {
        try {
            return groceryProductRepository.findAll();
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos do supermercado: " + e.getMessage(), e);
        }
    }

    public List<GroceryProduct> getAvailableProducts() {
        try {
            return groceryProductRepository.findAvailableProducts();
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos disponíveis: " + e.getMessage(), e);
        }
    }

    public List<GroceryProduct> getPriorityProducts() {
        try {
            return groceryProductRepository.findPriorityProducts();
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos prioritários: " + e.getMessage(), e);
        }
    }

    public List<GroceryProduct> getProductsWithActivePromotions() {
        try {
            LocalDate today = LocalDate.now();
            return groceryProductRepository.findProductsWithActivePromotions(today);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos em promoção: " + e.getMessage(), e);
        }
    }

    public List<GroceryProduct> getByCategory(Category category) {
        try {
            return groceryProductRepository.findByCategory(category);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos por categoria: " + e.getMessage(), e);
        }
    }

    public List<GroceryProduct> getByBrand(String brand) {
        try {
            return groceryProductRepository.findByBrand(brand);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos por marca: " + e.getMessage(), e);
        }
    }

    public GroceryProduct getById(Long id) {
        if (id == null) {
            throw new ProductValidationException("ID do produto não pode ser nulo");
        }

        try {
            return groceryProductRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));
        } catch (ProductNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produto com ID " + id + ": " + e.getMessage(), e);
        }
    }

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

        // Buscar e definir categoria
        Category category = categoryRepository.findById(dto.categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(dto.categoryId));
        product.setCategory(category);
    }
}
