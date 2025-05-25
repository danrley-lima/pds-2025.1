from pydantic import BaseModel
from app.models.enums import UnitTypeEnum
from datetime import date

class ProductInput(BaseModel):
    id: int
    name: str
    brand: str
    quantity: float
    unitWeight: float
    unitType: UnitTypeEnum
    stockQuantity: float
    unitPrice: float
    available: bool
    categoryName: str

class PromotionInput(BaseModel):
    id: int
    description: str
    productName: str
    originalPrice: float
    promotionalPrice: float
    productId: int
    initialDate: date
    finalDate: date

class recommendationRequest(BaseModel):
    text: str
    products: list[ProductInput]
    promotions: list[ProductInput]

