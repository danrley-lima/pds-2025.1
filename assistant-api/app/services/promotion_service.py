import re
from models.request import RecommendationRequest
from models.response import RecommendationResponse
from llm.promotion_llm import search_promotion_llm


def search_promotion(request: RecommendationRequest) -> RecommendationResponse:
    products, not_found_products = search_promotion_llm(request.customer_message, request.products, request.promotions)
    return RecommendationResponse(products=products, not_found_products=not_found_products)
