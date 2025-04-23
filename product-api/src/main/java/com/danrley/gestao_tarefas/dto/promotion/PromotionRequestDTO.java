package com.danrley.gestao_tarefas.dto.promotion;

import java.time.LocalDate;

public class PromotionRequestDTO {
    public Long productId;
    public double promotionalPrice;
    public LocalDate startDate;
    public LocalDate endDate;
}
