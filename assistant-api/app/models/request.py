from pydantic import BaseModel, Field
from models.enums import UnitTypeEnum
from datetime import date
from typing import List

class ProductInput(BaseModel):
    id: int
    name: str
    brand: str
    unit_weight: float = Field(..., alias="unitWeight")
    unit_type: UnitTypeEnum = Field(..., alias="unitType")
    stock_quantity: float = Field(..., alias="stockQuantity")
    unit_price: float = Field(..., alias="unitPrice")
    available: bool
    priority: bool
    category_name: str = Field(..., alias="categoryName")


class PromotionInput(BaseModel):
    id: int
    description: str
    product_name: str = Field(..., alias="productName")
    original_price: float = Field(..., alias="originalPrice")
    promotional_price: float = Field(..., alias="promotionalPrice")
    product_id: int = Field(..., alias="productId")
    initial_date: date = Field(..., alias="initialDate")
    final_date: date = Field(..., alias="finalDate")

class RecommendationRequest(BaseModel):
    customer_message: str
    products: List[ProductInput]
    promotions: List[PromotionInput]

