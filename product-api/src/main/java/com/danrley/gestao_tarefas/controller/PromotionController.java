package com.danrley.gestao_tarefas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping
    public List<PromotionResponseDTO> getAll() {
        return promotionService.getAll();
    }

    @GetMapping("/{id}")
    public PromotionResponseDTO getById(@PathVariable Long id) {
        return promotionService.getById(id);
    }

    @PostMapping
    public PromotionResponseDTO create(@RequestBody PromotionRequestDTO dto) {
        return promotionService.create(dto);
    }

    @PutMapping("/{id}")
    public PromotionResponseDTO update(@PathVariable Long id, @RequestBody PromotionRequestDTO dto) {
        return promotionService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        promotionService.delete(id);
    }

    @GetMapping("/promocoes-ativas")
    public List<PromotionResponseDTO> promocoesAtivas() {
        return promotionService.promocoesAtivas();
    }
}
