from app.models.request import RecommendationRequest
from app.models.response import RecommendationResponse
from app.llm.promotion_llm import PromotionLLMHandler


def search_promotion(request: RecommendationRequest) -> RecommendationResponse:
    handler = PromotionLLMHandler()
    products, not_found_products = handler.search_products(request.customer_message, request.products)
    return RecommendationResponse(products=products, not_found_products=not_found_products)
