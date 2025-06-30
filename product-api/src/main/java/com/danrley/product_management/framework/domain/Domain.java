package com.danrley.product_management.framework.domain;

/**
 * Enumeração que define os domínios suportados pelo sistema.
 * Cada domínio representa um tipo específico de negócio (ex: supermercado, móveis, construção).
 */
public enum Domain {
    GROCERY("grocery", "Supermercado"),
    FURNITURE("furniture", "Móveis e Decoração"),
    CONSTRUCTION("construction", "Material de Construção");

    private final String code;
    private final String displayName;

    Domain(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Busca um domínio pelo código.
     */
    public static Domain fromCode(String code) {
        for (Domain domain : values()) {
            if (domain.code.equals(code)) {
                return domain;
            }
        }
        throw new IllegalArgumentException("Unknown domain code: " + code);
    }
}
