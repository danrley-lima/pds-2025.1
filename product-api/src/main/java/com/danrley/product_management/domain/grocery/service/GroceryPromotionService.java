package com.danrley.product_management.domain.grocery.service;

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
import com.danrley.product_management.domain.grocery.model.GroceryProduct;
import com.danrley.product_management.domain.grocery.model.GroceryPromotion;
import com.danrley.product_management.domain.grocery.repository.GroceryPromotionRepository;

import jakarta.transaction.Transactional;

@Service
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

    @Transactional
    public List<GroceryPromotion> createPromotion(List<GroceryProduct> products, double discountPercentage) {
        if (products == null || products.isEmpty()) {
            throw new ProductValidationException("Lista de produtos não pode ser nula ou vazia");
        }

        if (discountPercentage <= 0 || discountPercentage >= 100) {
            throw new ProductValidationException("Percentual de desconto inválido. Deve estar entre 0 e 100.");
        }

        try {
            List<GroceryPromotion> promotions = new ArrayList<>();

            for (GroceryProduct product : products) {
                GroceryPromotion promotion = new GroceryPromotion();

                double promotionalPrice = product.getUnitPrice() * (1 - discountPercentage / 100);

                promotion.setGroceryProduct(product);
                promotion.setPromotionalPrice(promotionalPrice);

                // Pode ajustar essas datas conforme necessário
                promotion.setStartDate(LocalDate.now());
                promotion.setEndDate(LocalDate.now().plusDays(7));

                promotion.setDescription("Promoção automática de " + discountPercentage + "%");

                promotions.add(promotion);
            }

            return groceryPromotionRepository.saveAll(promotions);
        } catch (Exception e) {
            throw new ProductServiceException("Erro ao criar promoções: " + e.getMessage(), e);
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
