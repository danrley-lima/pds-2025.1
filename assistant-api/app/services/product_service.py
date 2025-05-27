from models.request import RecommendationRequest
from models.response import RecommendationResponse
from llm.product_llm import ProductLLMHandler


def search_product(request: RecommendationRequest) -> RecommendationResponse:
    handler = ProductLLMHandler()
    products, not_found_products = handler.search_products(request.customer_message, request.products)
    return RecommendationResponse(products=products, not_found_products=not_found_products)
