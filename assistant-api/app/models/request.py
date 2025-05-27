from pydantic import BaseModel, Field
from app.models.enums import UnitTypeEnum
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
    on_promotion: bool = Field(..., alias="onPromotion")
    promotional_price: float = Field(None, alias="promotionalPrice")


class RecommendationRequest(BaseModel):
    customer_message: str
    products: List[ProductInput]

