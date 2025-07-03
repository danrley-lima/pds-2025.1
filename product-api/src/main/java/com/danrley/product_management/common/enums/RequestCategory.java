package com.danrley.product_management.common.enums;

public enum RequestCategory {
    RECIPE("recipe"),
    SEARCH_PRODUCT("searchProduct"),
    SEARCH_PROMOTION("searchPromotion"),
    UNKNOWN("unknown");

    private final String value;

    RequestCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RequestCategory fromValue(String value) {
        for (RequestCategory category : RequestCategory.values()) {
            if (category.getValue().equalsIgnoreCase(value)) {
                return category;
            }
        }
        return UNKNOWN;
    }
}
