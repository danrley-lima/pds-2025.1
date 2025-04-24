package com.danrley.gestao_tarefas.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danrley.gestao_tarefas.dto.promotion.PromotionRequestDTO;
import com.danrley.gestao_tarefas.dto.promotion.PromotionResponseDTO;
import com.danrley.gestao_tarefas.model.product.Product;
import com.danrley.gestao_tarefas.model.promotion.Promotion;
import com.danrley.gestao_tarefas.repository.ProductRepository;
import com.danrley.gestao_tarefas.repository.PromotionRepository;

@Service
public class PromotionService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private PromotionRepository promotionRepo;

    public PromotionResponseDTO create(PromotionRequestDTO dto) {
        Promotion promotion = new Promotion();
        mapRequestToPromotion(dto, promotion);
        return toResponse(promotionRepo.save(promotion));
    }

    public List<PromotionResponseDTO> listarItensEmPromocao() {
        LocalDate hoje = LocalDate.now();
        return promotionRepo.findByEndDateAfter(hoje).stream().map(promo -> {
            return toResponse(promo);
        }).collect(Collectors.toList());
    }

    private void mapRequestToPromotion(PromotionRequestDTO dto, Promotion promocao) {
        if (dto.promotionalPrice != 0)
          promocao.setPromotionalPrice(dto.promotionalPrice);
        if (dto.startDate != null)
          promocao.setStartDate(dto.startDate);
        if (dto.endDate != null)
          promocao.setEndDate(dto.endDate);
        Product produto = productRepo.findById(dto.productId)
          .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        promocao.setProduct(produto);
    }

    private PromotionResponseDTO toResponse(Promotion promotion) {
        PromotionResponseDTO dto = new PromotionResponseDTO();
        dto.id = promotion.getId();
        dto.originalPrice = promotion.getProduct().getUnitPrice();
        dto.promotionalPrice = promotion.getPromotionalPrice();
        dto.endDate = promotion.getEndDate();
        dto.productName = promotion.getProduct() != null ? promotion.getProduct().getName() : null;
        return dto;
    }
}
