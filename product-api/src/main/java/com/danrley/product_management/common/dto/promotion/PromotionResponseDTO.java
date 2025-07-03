package com.danrley.product_management.common.dto.promotion;

import java.time.LocalDate;

public class PromotionResponseDTO {
    public Long id;
    public String description;
    public String productName;
    public double originalPrice;
    public double promotionalPrice;
    public LocalDate initialDate;
    public LocalDate finalDate;
}
