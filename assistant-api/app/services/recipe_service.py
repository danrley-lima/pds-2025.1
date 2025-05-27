from app.models.request import RecommendationRequest
from app.models.response import RecommendationResponse
from app.llm.recipe_llm import RecipeLLMHandler


def recipe_products(request: RecommendationRequest) -> RecommendationResponse:
    handler = RecipeLLMHandler()
    products, not_found_products = handler.search_products(request.customer_message, request.products)
    return RecommendationResponse(products=products, not_found_products=not_found_products)