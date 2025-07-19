package com.danrley.product_management.common.dto.promotionCondition;

import java.time.LocalDate;
import java.util.List;

public class PromotionConditionRequestDTO {
    public String description;
    public List<Long> productIds;
    public Long promotionConditionId;
    public double discountPercentage;
    public String domain;
    public Integer daysBeforeExpiry;
    public Long categoryId;
}
