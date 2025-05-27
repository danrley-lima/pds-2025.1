from models.request import RecommendationRequest
from models.response import RecommendationResponse
from llm.recipe_llm import recipe_products_llm


def recipe_products(request: RecommendationRequest) -> RecommendationResponse:
    products, not_found_products = recipe_products_llm(request.customer_message, request.products, request.promotions)
    return RecommendationResponse(products=products, not_found_products=not_found_products)