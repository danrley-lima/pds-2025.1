package com.danrley.gestao_tarefas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danrley.gestao_tarefas.dto.promotion.PromotionResponseDTO;
import com.danrley.gestao_tarefas.dto.promotion.PromotionRequestDTO;
import com.danrley.gestao_tarefas.service.PromotionService;

import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/promotions")
public class PromotionController {
    @Autowired
    private PromotionService promotionService;

    @PostMapping
    public PromotionResponseDTO create(@RequestBody PromotionRequestDTO dto) {
        System.out.println("Product ID received: " + dto.productId);
        return promotionService.create(dto);
    }

    @GetMapping
    public ResponseEntity<List<PromotionResponseDTO>> listarPromocoes() {
        return ResponseEntity.ok(promotionService.listarItensEmPromocao());
    }
}
