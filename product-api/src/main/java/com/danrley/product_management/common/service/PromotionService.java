package com.danrley.product_management.common.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danrley.product_management.common.dto.promotion.PromotionRequestDTO;
import com.danrley.product_management.common.dto.promotion.PromotionResponseDTO;
import com.danrley.product_management.common.exception.custom.ProductNotFoundException;
import com.danrley.product_management.common.exception.custom.PromotionNotFoundException;
import com.danrley.product_management.common.model.promotion.Promotion;
import com.danrley.product_management.common.repository.PromotionRepository;
import com.danrley.product_management.domain.construction.model.ConstructionProduct;
import com.danrley.product_management.domain.construction.repository.ConstructionProductRepository;
import com.danrley.product_management.domain.furniture.model.FurnitureProduct;
import com.danrley.product_management.domain.furniture.repository.FurnitureProductRepository;
import com.danrley.product_management.domain.grocery.model.GroceryProduct;
import com.danrley.product_management.domain.grocery.repository.GroceryProductRepository;

/**
 * Serviço para promoções.
 * Criação, atualização e consulta de promoções para produtos de todos
 * os domínios.
 */
@Service
public class PromotionService {

  @Autowired
  private PromotionRepository promotionRepository;

  @Autowired
  private GroceryProductRepository groceryProductRepository;

  @Autowired
  private FurnitureProductRepository furnitureProductRepository;

  @Autowired
  private ConstructionProductRepository constructionProductRepository;

  public List<PromotionResponseDTO> getAll() {
    return promotionRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
  }

  public PromotionResponseDTO getById(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("ID da promoção não pode ser nulo");
    }

    Promotion promotion = promotionRepository.findById(id)
        .orElseThrow(() -> new PromotionNotFoundException("Promoção não encontrada com ID: " + id));

    return toResponse(promotion);
  }

  public void delete(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("ID da promoção não pode ser nulo");
    }

    if (!promotionRepository.existsById(id)) {
      throw new PromotionNotFoundException("Promoção não encontrada com ID: " + id);
    }

    promotionRepository.deleteById(id);
  }

  public PromotionResponseDTO create(PromotionRequestDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("Dados da promoção não podem ser nulos");
    }

    Promotion promotion = new Promotion();
    promotion.setPromotionalPrice(dto.promotionalPrice);
    promotion.setDescription(dto.description);
    promotion.setStartDate(dto.startDate);
    promotion.setEndDate(dto.endDate);

    Promotion savedPromotion = promotionRepository.save(promotion);
    return toResponse(savedPromotion);
  }

  public PromotionResponseDTO update(Long id, PromotionRequestDTO dto) {
    if (id == null) {
      throw new IllegalArgumentException("ID da promoção não pode ser nulo");
    }
    if (dto == null) {
      throw new IllegalArgumentException("Dados da promoção não podem ser nulos");
    }

    Promotion promotion = promotionRepository.findById(id)
        .orElseThrow(() -> new PromotionNotFoundException("Promoção não encontrada com ID: " + id));

    promotion.setPromotionalPrice(dto.promotionalPrice);
    promotion.setDescription(dto.description);
    promotion.setStartDate(dto.startDate);
    promotion.setEndDate(dto.endDate);



    Promotion updatedPromotion = promotionRepository.save(promotion);
    return toResponse(updatedPromotion);
  }

  public List<PromotionResponseDTO> getActivePromotions() {
    LocalDate today = LocalDate.now();
    return promotionRepository.findAll().stream()
        .filter(promotion -> promotion.getStartDate().isBefore(today.plusDays(1)) &&
            promotion.getEndDate().isAfter(today.minusDays(1)))
        .map(this::toResponse)
        .collect(Collectors.toList());
  }

  private PromotionResponseDTO toResponse(Promotion promotion) {
    PromotionResponseDTO dto = new PromotionResponseDTO();
    dto.id = promotion.getId();
    dto.promotionalPrice = promotion.getPromotionalPrice();
    dto.initialDate = promotion.getStartDate();
    dto.finalDate = promotion.getEndDate();
    dto.description = promotion.getDescription();
    dto.productName = promotion.getBaseProduct() != null ? promotion.getBaseProduct().getName() : null;
    dto.originalPrice = promotion.getBaseProduct() != null ? promotion.getBaseProduct().getUnitPrice() : 0.0;

    return dto;
  }
}
