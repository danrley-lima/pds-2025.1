package com.danrley.product_management.domain.grocery.controller;

@RestController
@RequestMapping("/api/grocery/promotions")
public class GroceryPromotionConditionController extends
        BasePromotionConditionController<GroceryPromotion, GroceryProduct, GroceryPromotionCondition, GroceryPromotionConditionService> {

    private final GroceryPromotionConditionService promotionConditionService;
    private final GroceryPromotionService promotionService;

    @Autowired
    public GroceryPromotionConditionController(
            GroceryPromotionConditionService promotionConditionService,
            GroceryPromotionService promotionService) {
        super(promotionConditionService, promotionService);
        this.promotionConditionService = promotionConditionService;
        this.promotionService = promotionService;
    }

    @PostMapping("/conditions")
    @Operation(summary = "Criar promoções por produto", description = "Cria promoções para os produtos informados com base no desconto fornecido.")
    public List<PromotionResponseDTO> create(
            @Parameter(description = "Dados da condição de promoção por produto") @RequestBody PromotionConditionRequestDTO dto) {

        List<GroceryPromotion> promotions = promotionConditionService.createPromotion(dto);
        return promotions.stream()
                .map(promotionService::toResponseDTO)
                .collect(Collectors.toList());
    }
}
