package com.danrley.product_management.dto.promotion;

import java.time.LocalDate;

public class PromotionRequestDTO {
    public double promotionalPrice;
    public String description;
    public LocalDate startDate;
    public LocalDate endDate;
    public Long productId;
}
