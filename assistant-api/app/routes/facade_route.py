import logging
from fastapi import APIRouter, HTTPException
from app.llm.selector import classify_text
from app.models.request import RecommendationRequest
from app.models.response import RecommendationResponse
from app.services.recipe_service import recipe_products
from app.services.product_service import search_product
from app.services.promotion_service import search_promotion


router = APIRouter()

@router.post("/recommendations", response_model=RecommendationResponse)
async def recommend_products(
    request: RecommendationRequest
) -> RecommendationResponse:
    try:
        category = classify_text(request.customer_message)
        print(f"Classified category: {category}")
        if category == "recipe":
            return recipe_products(request)
        elif category == "searchProduct":
            return search_product(request)
        elif category == "searchPromotion":
            return search_promotion(request)
        else:
            raise HTTPException(status_code=404, detail="Request category not recognized")

    except HTTPException as e:
        raise e
    except Exception as e:
        logging.exception("Unexpected error in recommend_products")
        raise HTTPException(status_code=500, detail="Internal server error")
