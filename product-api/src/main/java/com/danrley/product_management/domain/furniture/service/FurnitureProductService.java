package com.danrley.product_management.domain.furniture.service;

import com.danrley.product_management.domain.furniture.model.FurnitureProduct;
import com.danrley.product_management.domain.furniture.repository.FurnitureProductRepository;
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
 * Serviço específico para produtos do domínio furniture.
 * Contém lógicas de negócio específicas para móveis.
 */
@Service
public class FurnitureProductService {

    @Autowired
    private FurnitureProductRepository furnitureProductRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<FurnitureProduct> getAll() {
        try {
            return furnitureProductRepository.findAll();
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos de móveis: " + e.getMessage(), e);
        }
    }

    public List<ProductResponseDTO> getAllAsDTO() {
        try {
            return furnitureProductRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos de móveis: " + e.getMessage(), e);
        }
    }

    public ProductResponseDTO getById(Long id) {
        FurnitureProduct product = furnitureProductRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Produto de móveis não encontrado com ID: " + id));
        return toResponse(product);
    }

    public List<ProductResponseDTO> getByIds(List<Long> ids) {
        try {
            return furnitureProductRepository.findAllById(ids)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos de móveis por IDs: " + e.getMessage(), e);
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

            FurnitureProduct product = new FurnitureProduct(
                request.name,
                request.brand,
                request.unitWeight,
                request.unitType,
                request.stockQuantity,
                request.unitPrice,
                category,
                request.available != null ? request.available : true,
                request.priority != null ? request.priority : false,
                request.dimensions,
                request.material,
                request.color,
                request.style
            );

            FurnitureProduct savedProduct = furnitureProductRepository.save(product);
            return toResponse(savedProduct);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao criar produto de móveis: " + e.getMessage(), e);
        }
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO request) {
        try {
            FurnitureProduct existingProduct = furnitureProductRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto de móveis não encontrado com ID: " + id));

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
            existingProduct.setDimensions(request.dimensions);
            existingProduct.setMaterial(request.material);
            existingProduct.setColor(request.color);
            existingProduct.setStyle(request.style);

            FurnitureProduct updatedProduct = furnitureProductRepository.save(existingProduct);
            return toResponse(updatedProduct);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao atualizar produto de móveis: " + e.getMessage(), e);
        }
    }

    public void delete(Long id) {
        try {
            if (!furnitureProductRepository.existsById(id)) {
                throw new ProductNotFoundException("Produto de móveis não encontrado com ID: " + id);
            }
            furnitureProductRepository.deleteById(id);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao deletar produto de móveis: " + e.getMessage(), e);
        }
    }

    // Métodos específicos do domínio furniture
    public List<ProductResponseDTO> getByMaterial(String material) {
        return furnitureProductRepository.findByMaterialContainingIgnoreCase(material)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getByColor(String color) {
        return furnitureProductRepository.findByColorContainingIgnoreCase(color)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getByStyle(String style) {
        return furnitureProductRepository.findByStyleContainingIgnoreCase(style)
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

    private ProductResponseDTO toResponse(FurnitureProduct product) {
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
        response.dimensions = product.getDimensions();
        response.material = product.getMaterial();
        response.color = product.getColor();
        response.style = product.getStyle();
        
        if (product.getCategory() != null) {
            response.categoryId = product.getCategory().getId();
            response.categoryName = product.getCategory().getName();
        }
        
        return response;
    }
}
