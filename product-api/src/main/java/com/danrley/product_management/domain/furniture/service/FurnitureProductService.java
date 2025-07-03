package com.danrley.product_management.domain.furniture.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danrley.product_management.core.service.BaseProductService;
import com.danrley.product_management.domain.furniture.model.FurnitureProduct;
import com.danrley.product_management.domain.furniture.repository.FurnitureProductRepository;
import com.danrley.product_management.common.dto.product.ProductRequestDTO;
import com.danrley.product_management.common.dto.product.ProductResponseDTO;
import com.danrley.product_management.common.exception.custom.CategoryNotFoundException;
import com.danrley.product_management.common.exception.custom.ProductNotFoundException;
import com.danrley.product_management.common.exception.custom.ProductServiceException;
import com.danrley.product_management.common.exception.custom.ProductValidationException;
import com.danrley.product_management.common.model.category.Category;
import com.danrley.product_management.common.repository.CategoryRepository;

/**
 * Serviço específico para produtos do domínio furniture.
 * Implementa BaseProductService para reaproveitamento de código do framework.
 */
@Service
public class FurnitureProductService implements BaseProductService<FurnitureProduct> {

    @Autowired
    private FurnitureProductRepository furnitureProductRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // ========== IMPLEMENTAÇÃO DOS MÉTODOS BASE ==========

    @Override
    public List<FurnitureProduct> getAll() {
        try {
            return furnitureProductRepository.findAll();
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos de móveis: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<FurnitureProduct> getById(Long id) {
        if (id == null) {
            throw new ProductValidationException("ID do produto não pode ser nulo");
        }

        try {
            return furnitureProductRepository.findById(id);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produto com ID " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public FurnitureProduct create(ProductRequestDTO dto) {
        try {
            validateProductRequest(dto, true);

            FurnitureProduct product = new FurnitureProduct();
            mapRequestToProduct(dto, product);

            return furnitureProductRepository.save(product);
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
    public FurnitureProduct update(Long id, ProductRequestDTO dto) {
        if (id == null) {
            throw new ProductValidationException("ID do produto não pode ser nulo");
        }

        try {
            validateProductRequest(dto, false);

            FurnitureProduct product = furnitureProductRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));

            mapRequestToProduct(dto, product);
            return furnitureProductRepository.save(product);
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
            FurnitureProduct product = furnitureProductRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));

            furnitureProductRepository.delete(product);
        } catch (ProductNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao deletar produto: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductResponseDTO toResponseDTO(FurnitureProduct entity) {
        return ProductResponseDTO.fromFurnitureProduct(entity);
    }

    @Override
    public List<FurnitureProduct> getByCategory(String category) {
        try {
            // Buscar categoria pelo nome
            Category categoryEntity = categoryRepository.findByName(category)
                    .orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada: " + category));
            return furnitureProductRepository.findByCategory(categoryEntity);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos por categoria: " + e.getMessage(), e);
        }
    }

    @Override
    public List<FurnitureProduct> getByBrand(String brand) {
        try {
            // Assumindo que existe este método no repository ou implementar uma busca alternativa
            return furnitureProductRepository.findAll().stream()
                    .filter(p -> p.getBrand() != null && p.getBrand().toLowerCase().contains(brand.toLowerCase()))
                    .toList();
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos por marca: " + e.getMessage(), e);
        }
    }

    @Override
    public List<FurnitureProduct> getAvailable() {
        try {
            return furnitureProductRepository.findByAvailableTrue();
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos disponíveis: " + e.getMessage(), e);
        }
    }

    // ========== MÉTODOS ESPECÍFICOS DO DOMÍNIO ==========
    
    /**
     * Busca produtos por material (específico do domínio furniture).
     */
    public List<FurnitureProduct> getByMaterial(String material) {
        try {
            // Implementar filtro customizado
            return furnitureProductRepository.findAll().stream()
                    .filter(p -> p.getMaterial() != null && p.getMaterial().toLowerCase().contains(material.toLowerCase()))
                    .toList();
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos por material: " + e.getMessage(), e);
        }
    }

    /**
     * Busca produtos por cor (específico do domínio furniture).
     */
    public List<FurnitureProduct> getByColor(String color) {
        try {
            return furnitureProductRepository.findAll().stream()
                    .filter(p -> p.getColor() != null && p.getColor().toLowerCase().contains(color.toLowerCase()))
                    .toList();
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos por cor: " + e.getMessage(), e);
        }
    }

    /**
     * Busca produtos por estilo (específico do domínio furniture).
     */
    public List<FurnitureProduct> getByStyle(String style) {
        try {
            return furnitureProductRepository.findAll().stream()
                    .filter(p -> p.getStyle() != null && p.getStyle().toLowerCase().contains(style.toLowerCase()))
                    .toList();
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar produtos por estilo: " + e.getMessage(), e);
        }
    }

    // ========== MÉTODOS AUXILIARES ==========

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

    private void mapRequestToProduct(ProductRequestDTO dto, FurnitureProduct product) {
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

        // Campos específicos do furniture
        if (dto.dimensions != null) {
            product.setDimensions(dto.dimensions);
        }
        
        if (dto.material != null) {
            product.setMaterial(dto.material);
        }
        
        if (dto.color != null) {
            product.setColor(dto.color);
        }
        
        if (dto.style != null) {
            product.setStyle(dto.style);
        }
    }
}
