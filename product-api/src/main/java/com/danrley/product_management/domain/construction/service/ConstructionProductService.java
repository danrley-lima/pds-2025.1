package com.danrley.product_management.domain.construction.service;

import com.danrley.product_management.domain.construction.model.ConstructionProduct;
import com.danrley.product_management.domain.construction.repository.ConstructionProductRepository;
import com.danrley.product_management.dto.product.ProductRequestDTO;
import com.danrley.product_management.dto.product.ProductResponseDTO;
import com.danrley.product_management.exception.custom.ProductNotFoundException;
import com.danrley.product_management.exception.custom.ProductServiceException;
import com.danrley.product_management.exception.custom.ProductValidationException;
import com.danrley.product_management.model.category.Category;
import com.danrley.product_management.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço específico para produtos do domínio construction.
 * Contém lógicas de negócio específicas para materiais de construção.
 */
@Service
public class ConstructionProductService {

    @Autowired
    private ConstructionProductRepository constructionProductRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ConstructionProduct> getAll() {
        try {
            return constructionProductRepository.findAll();
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos de construção: " + e.getMessage(), e);
        }
    }

    public List<ProductResponseDTO> getAllAsDTO() {
        try {
            return constructionProductRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos de construção: " + e.getMessage(), e);
        }
    }

    public ProductResponseDTO getById(Long id) {
        ConstructionProduct product = constructionProductRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Produto de construção não encontrado com ID: " + id));
        return toResponse(product);
    }

    public List<ProductResponseDTO> getByIds(List<Long> ids) {
        try {
            return constructionProductRepository.findAllById(ids)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos de construção por IDs: " + e.getMessage(), e);
        }
    }

    public ProductResponseDTO create(ProductRequestDTO request) {
        try {
            validateRequest(request);
            
            Category category = null;
            if (request.categoryId != null) {
                category = categoryRepository.findById(request.categoryId)
                    .orElseThrow(() -> new ProductNotFoundException("Categoria não encontrada com ID: " + request.categoryId));
            }

            ConstructionProduct product = new ConstructionProduct(
                request.name,
                request.brand,
                request.unitWeight,
                request.unitType,
                request.stockQuantity,
                request.unitPrice,
                category,
                request.available != null ? request.available : true,
                request.priority != null ? request.priority : false,
                request.specifications,
                request.constructionCategory,
                request.application,
                request.grade
            );

            ConstructionProduct savedProduct = constructionProductRepository.save(product);
            return toResponse(savedProduct);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao criar produto de construção: " + e.getMessage(), e);
        }
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO request) {
        try {
            ConstructionProduct existingProduct = constructionProductRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto de construção não encontrado com ID: " + id));

            validateRequest(request);

            Category category = null;
            if (request.categoryId != null) {
                category = categoryRepository.findById(request.categoryId)
                    .orElseThrow(() -> new ProductNotFoundException("Categoria não encontrada com ID: " + request.categoryId));
            }

            // Atualizar campos
            existingProduct.setName(request.name);
            existingProduct.setBrand(request.brand);
            existingProduct.setUnitWeight(request.unitWeight);
            existingProduct.setUnitType(request.unitType);
            existingProduct.setStockQuantity(request.stockQuantity);
            existingProduct.setUnitPrice(request.unitPrice);
            existingProduct.setCategory(category);
            existingProduct.setAvailable(request.available != null ? request.available : true);
            existingProduct.setPriority(request.priority != null ? request.priority : false);
            existingProduct.setSpecifications(request.specifications);
            existingProduct.setConstructionCategory(request.constructionCategory);
            existingProduct.setApplication(request.application);
            existingProduct.setGrade(request.grade);

            ConstructionProduct updatedProduct = constructionProductRepository.save(existingProduct);
            return toResponse(updatedProduct);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao atualizar produto de construção: " + e.getMessage(), e);
        }
    }

    public void delete(Long id) {
        try {
            if (!constructionProductRepository.existsById(id)) {
                throw new ProductNotFoundException("Produto de construção não encontrado com ID: " + id);
            }
            constructionProductRepository.deleteById(id);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao deletar produto de construção: " + e.getMessage(), e);
        }
    }

    // Métodos específicos do domínio construction
    public List<ProductResponseDTO> getByApplication(String application) {
        return constructionProductRepository.findByApplicationContainingIgnoreCase(application)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getByGrade(String grade) {
        return constructionProductRepository.findByGradeContainingIgnoreCase(grade)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getBySpecifications(String specifications) {
        return constructionProductRepository.findBySpecificationsContainingIgnoreCase(specifications)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    private void validateRequest(ProductRequestDTO request) {
        if (request.name == null || request.name.trim().isEmpty()) {
            throw new ProductValidationException("Nome do produto é obrigatório");
        }
        if (request.unitPrice == null || request.unitPrice < 0) {
            throw new ProductValidationException("Preço unitário deve ser maior ou igual a zero");
        }
        if (request.stockQuantity == null || request.stockQuantity < 0) {
            throw new ProductValidationException("Quantidade em estoque deve ser maior ou igual a zero");
        }
    }

    private ProductResponseDTO toResponse(ConstructionProduct product) {
        ProductResponseDTO response = new ProductResponseDTO();
        response.id = product.getId();
        response.name = product.getName();
        response.brand = product.getBrand();
        response.unitWeight = product.getUnitWeight();
        response.unitType = product.getUnitType();
        response.stockQuantity = product.getStockQuantity();
        response.unitPrice = product.getUnitPrice();
        response.available = product.isAvailable();
        response.priority = product.isPriority();
        response.specifications = product.getSpecifications();
        response.constructionCategory = product.getConstructionCategory();
        response.application = product.getApplication();
        response.grade = product.getGrade();
        
        if (product.getCategory() != null) {
            response.categoryId = product.getCategory().getId();
            response.categoryName = product.getCategory().getName();
        }
        
        return response;
    }
}
