package com.danrley.product_management.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danrley.product_management.dto.promotion.PromotionRequestDTO;
import com.danrley.product_management.dto.promotion.PromotionResponseDTO;
import com.danrley.product_management.exception.custom.ProductNotFoundException;
import com.danrley.product_management.exception.custom.ProductValidationException;
import com.danrley.product_management.exception.custom.PromotionNotFoundException;
import com.danrley.product_management.model.product.Product;
import com.danrley.product_management.model.promotion.Promotion;
import com.danrley.product_management.repository.ProductRepository;
import com.danrley.product_management.repository.PromotionRepository;

@Service
public class PromotionService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private PromotionRepository promotionRepository;

  public List<PromotionResponseDTO> getAll() {
    return promotionRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
  }

  public PromotionResponseDTO getById(Long id) {
    return promotionRepository.findById(id)
        .map(this::toResponse)
        .orElseThrow(() -> new PromotionNotFoundException(id));
  }

  public PromotionResponseDTO create(PromotionRequestDTO dto) {
    validatePromotionRequest(dto);
    Promotion promotion = new Promotion();
    mapRequestToPromotion(dto, promotion);
    return toResponse(promotionRepository.save(promotion));
  }

  public PromotionResponseDTO update(Long id, PromotionRequestDTO dto) {
    validatePromotionRequest(dto);
    Promotion promotion = promotionRepository.findById(id)
        .orElseThrow(() -> new PromotionNotFoundException(id));
    mapRequestToPromotion(dto, promotion);
    return toResponse(promotionRepository.save(promotion));
  }

  public void delete(Long id) {
    Promotion promotion = promotionRepository.findById(id)
        .orElseThrow(() -> new PromotionNotFoundException(id));

    Product product = promotion.getProduct();
    if (product != null) {
      product.setPromotion(null);
      productRepository.save(product);
    }

    promotionRepository.delete(promotion);
  }

  public List<PromotionResponseDTO> promocoesAtivas() {
    LocalDate hoje = LocalDate.now();
    return promotionRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(hoje, hoje)
        .stream().map(this::toResponse).collect(Collectors.toList());
  }

  private void validatePromotionRequest(PromotionRequestDTO dto) {
    if (dto.promotionalPrice <= 0) {
      throw new ProductValidationException("O preço promocional deve ser maior que zero");
    }

    if (dto.startDate != null && dto.endDate != null && dto.startDate.isAfter(dto.endDate)) {
      throw new ProductValidationException("A data inicial não pode ser posterior à data final");
    }
  }

  private void mapRequestToPromotion(PromotionRequestDTO dto, Promotion promocao) {
    if (dto.promotionalPrice != 0)
      promocao.setPromotionalPrice(dto.promotionalPrice);
    if (dto.startDate != null)
      promocao.setStartDate(dto.startDate);
    if (dto.endDate != null)
      promocao.setEndDate(dto.endDate);
    if (dto.description != null)
      promocao.setDescription(dto.description);
    Product produto = productRepository.findById(dto.productId)
        .orElseThrow(() -> new ProductNotFoundException(dto.productId));
    promocao.setProduct(produto);
  }

  private PromotionResponseDTO toResponse(Promotion promotion) {
    PromotionResponseDTO dto = new PromotionResponseDTO();
    dto.id = promotion.getId();
    dto.originalPrice = promotion.getProduct().getUnitPrice();
    dto.description = promotion.getDescription();
    dto.promotionalPrice = promotion.getPromotionalPrice();
    dto.initialDate = promotion.getStartDate();
    dto.finalDate = promotion.getEndDate();
    dto.productName = promotion.getProduct() != null ? promotion.getProduct().getName() : null;
    return dto;
  }
}
