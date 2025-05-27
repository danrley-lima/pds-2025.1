from models.request import RecommendationRequest
from models.response import RecommendationResponse
from llm.product_llm import search_product_llm


def search_product(request: RecommendationRequest) -> RecommendationResponse:
    products, not_found_products = search_product_llm(request.customer_message, request.products, request.promotions)
    return RecommendationResponse(products=products, not_found_products=not_found_products)
