package com.danrley.product_management.domain.grocery.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.danrley.product_management.common.dto.promotion.PromotionRequestDTO;
import com.danrley.product_management.common.dto.promotion.PromotionResponseDTO;
import com.danrley.product_management.common.exception.custom.ProductNotFoundException;
import com.danrley.product_management.common.exception.custom.ProductServiceException;
import com.danrley.product_management.common.exception.custom.ProductValidationException;
import com.danrley.product_management.core.service.BasePromotionService;
import com.danrley.product_management.domain.grocery.model.GroceryPromotion;
import com.danrley.product_management.domain.grocery.repository.GroceryPromotionRepository;

import jakarta.transaction.Transactional;

public class GroceryPromotionService implements BasePromotionService<GroceryPromotion> {

    @Autowired
    private GroceryPromotionRepository groceryPromotionRepository;

    @Override
    public List<GroceryPromotion> getAll() {
        try {
            return groceryPromotionRepository.findAll();
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar promoções do supermercado: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<GroceryPromotion> getById(Long id) {
        if (id == null) {
            throw new ProductValidationException("ID da promoção não pode ser nulo");
        }

        try {
            return groceryPromotionRepository.findById(id);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao buscar promoção com ID " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public GroceryPromotion create(PromotionRequestDTO dto) {
        try {
            GroceryPromotion promotion = new GroceryPromotion();
            mapRequestToProduct(dto, promotion);

            return groceryPromotionRepository.save(promotion);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao criar promoção: " + e.getMessage(), e);
        }
    }

    @Override
    public GroceryPromotion update(Long id, PromotionRequestDTO dto) {
        try {
            GroceryPromotion promotion = groceryPromotionRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));

            mapRequestToProduct(dto, promotion);
            return groceryPromotionRepository.save(promotion);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao atualizar promoção: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            GroceryPromotion promotion = groceryPromotionRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));

            groceryPromotionRepository.delete(promotion);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao deletar promoção: " + e.getMessage(), e);
        }
    }

    @Override
    public PromotionResponseDTO toResponseDTO(GroceryPromotion entity) {
        return PromotionResponseDTO.fromGroceryPromotion(entity);
    }

    private void mapRequestToProduct(PromotionRequestDTO dto, GroceryPromotion promotion) {
        promotion.setPromotionalPrice(dto.promotionalPrice);
        promotion.setStartDate(dto.startDate);
        promotion.setEndDate(dto.endDate);
        promotion.setDescription(dto.description);
    }

}
