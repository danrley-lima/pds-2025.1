package com.danrley.product_management.domain.grocery.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danrley.product_management.core.model.BaseProduct;
import com.danrley.product_management.core.model.BasePromotion;
import com.danrley.product_management.core.model.BasePromotionCondition;
import com.danrley.product_management.domain.grocery.model.GroceryProduct;
import com.danrley.product_management.domain.grocery.model.GroceryPromotion;
import com.danrley.product_management.domain.grocery.model.GroceryPromotionCondition;
import com.danrley.product_management.domain.grocery.repository.GroceryPromotionConditionRepository;
import com.danrley.product_management.domain.grocery.repository.GroceryPromotionRepository;
import com.danrley.product_management.core.service.BasePromotionConditionService;


@Service
public class GroceryPromotionConditionService implements BasePromotionConditionService<GroceryProduct> {

    private static final Logger logger = LoggerFactory.getLogger(GroceryPromotionConditionService.class);

    @Autowired
    private GroceryPromotionConditionRepository groceryPromotionConditionRepository;

    @Autowired
    private GroceryPromotionRepository GroceryPromotionRepository;

    @Override
    public GroceryPromotionCondition createPromotion(List<BaseProduct> products) {
        try {
            GroceryPromotionCondition condition = new GroceryPromotionCondition();

            // Filtra apenas produtos de grocery
            List<GroceryProduct> groceryProducts = products.stream()
                    .filter(product -> product instanceof GroceryProduct)
                    .map(product -> (GroceryProduct) product)
                    .collect(Collectors.toList());
            
            applyPromotion(groceryProducts);
            
            return groceryPromotionConditionRepository.save(condition);
        } catch (Exception e) {
            logger.error("Erro ao criar condições de promoção: {}", e.getMessage());
            return new GroceryPromotionCondition();
        }
    }

    @Override
    public GroceryPromotionCondition findById(Long id) {
        return groceryPromotionConditionRepository.findById(id).orElse(null);
    }


    @Override
    public void deleteById(Long id) {
        groceryPromotionConditionRepository.deleteById(id);
    }

    @Override
    public List<GroceryProduct> getEligibleProducts(List<GroceryProduct> products) {
        GroceryPromotionCondition groceryPromotionCondition = new GroceryPromotionCondition();
        if (products == null || products.isEmpty()) {
            return new ArrayList<>();
        }
        
        return products.stream()
                .filter(groceryPromotionCondition::isEligible) // <--- Aqui está a mudança!
                .collect(Collectors.toList());
    }

    
    private void applyPromotion(List<GroceryProduct> products) {
        // Validação básica da lista
        if (products == null || products.isEmpty()) {
            logger.warn("Tentativa de aplicar promoção em lista vazia ou nula");
            return;
        }

        if (products.isEmpty()) {
            logger.info("Nenhum produto elegível encontrado para aplicação de promoção");
            return;
        }

        logger.info("Aplicando promoções para {} produtos próximos ao vencimento", products.size());
        
        // Aplica promoção para cada produto elegível
        for (GroceryProduct product : products) {
            try {
                GroceryPromotion promotion = new GroceryPromotion();
                promotion.setGroceryProduct(product);
                GroceryPromotionRepository.save(promotion);
            } catch (Exception e) {
                logger.error("Erro ao aplicar promoção no produto ID {}: {}", 
                            product.getId(), e.getMessage());
            }
        }
        
        logger.info("Promoções aplicadas com sucesso para {} produtos", products.size());
    }
}
