package com.danrley.gestao_tarefas.dto.promotion;

import java.time.LocalDate;

public class ProductWithPromotionResponse {
    public String productName;
    public double originalPrice;
    public double promotionalPrice;
    public LocalDate endDate;
}
