package com.danrley.product_management.domain.construction.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danrley.product_management.common.dto.promotion.PromotionRequestDTO;
import com.danrley.product_management.common.dto.promotion.PromotionResponseDTO;
import com.danrley.product_management.common.exception.custom.ProductNotFoundException;
import com.danrley.product_management.common.exception.custom.ProductServiceException;
import com.danrley.product_management.common.exception.custom.ProductValidationException;
import com.danrley.product_management.core.service.BasePromotionService;
import com.danrley.product_management.domain.construction.model.ConstructionProduct;
import com.danrley.product_management.domain.construction.model.ConstructionPromotion;
import com.danrley.product_management.domain.construction.repository.ConstructionProductRepository;
import com.danrley.product_management.domain.construction.repository.ConstructionPromotionRepository;

import jakarta.transaction.Transactional;

@Service
public class ConstructionPromotionService implements BasePromotionService<ConstructionPromotion> {

    @Autowired
    private ConstructionPromotionRepository constructionPromotionRepository;

    @Autowired
    private ConstructionProductRepository constructionProductRepository;

    @Override
    public List<ConstructionPromotion> getAll() {
        try {
            return constructionPromotionRepository.findAll();
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar promoções de material de construção: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<ConstructionPromotion> getById(Long id) {
        if (id == null) {
            throw new ProductValidationException("ID da promoção não pode ser nulo");
        }

        try {
            return constructionPromotionRepository.findById(id);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar promoção com ID " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public ConstructionPromotion create(PromotionRequestDTO dto) {
        try {
            ConstructionPromotion promotion = new ConstructionPromotion();
            mapRequestToPromotion(dto, promotion);

            return constructionPromotionRepository.save(promotion);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao criar promoção: " + e.getMessage(), e);
        }
    }

    @Override
    public ConstructionPromotion update(Long id, PromotionRequestDTO dto) {
        try {
            ConstructionPromotion promotion = constructionPromotionRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));

            mapRequestToPromotion(dto, promotion);
            return constructionPromotionRepository.save(promotion);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao atualizar promoção: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            ConstructionPromotion promotion = constructionPromotionRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));

            constructionPromotionRepository.delete(promotion);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao deletar promoção: " + e.getMessage(), e);
        }
    }

    @Transactional
    public List<ConstructionPromotion> createPromotion(Long categoryId, double discountPercentage) {
        if (categoryId == null) {
            throw new ProductValidationException("ID da categoria não pode ser nulo");
        }

        if (discountPercentage <= 0 || discountPercentage >= 100) {
            throw new ProductValidationException("Percentual de desconto inválido. Deve estar entre 0 e 100.");
        }

        try {
            List<ConstructionProduct> products = constructionProductRepository.findByCategoryId(categoryId);

            if (products.isEmpty()) {
                throw new ProductNotFoundException("Nenhum produto encontrado para a categoria ID: " + categoryId);
            }

            List<ConstructionPromotion> promotions = new ArrayList<>();

            for (ConstructionProduct product : products) {
                ConstructionPromotion promotion = new ConstructionPromotion();

                double promotionalPrice = product.getUnitPrice() * (1 - discountPercentage / 100.0);

                promotion.setConstructionProduct(product);
                promotion.setPromotionalPrice(promotionalPrice);
                promotion.setStartDate(LocalDate.now());
                promotion.setEndDate(LocalDate.now().plusDays(10)); // Ajuste conforme regra
                promotion.setDescription("Promoção da categoria " + product.getCategory().getName());

                promotions.add(promotion);
            }

            return constructionPromotionRepository.saveAll(promotions);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao criar promoções para categoria: " + e.getMessage(), e);
        }
    }

    @Override
    public PromotionResponseDTO toResponseDTO(ConstructionPromotion entity) {
        return PromotionResponseDTO.fromConstructionPromotion(entity);
    }

    private void mapRequestToPromotion(PromotionRequestDTO dto, ConstructionPromotion promotion) {
        promotion.setPromotionalPrice(dto.promotionalPrice);
        promotion.setStartDate(dto.startDate);
        promotion.setEndDate(dto.endDate);
        promotion.setDescription(dto.description);
    }
}
