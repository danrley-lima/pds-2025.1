from pydantic import BaseModel, Field
from typing import List
from typing import Optional

class ProductOut(BaseModel):
    id: str
    name: str
    brand: str
    category_name: str = Field(..., alias="categoryName")
    unit_price: str = Field(..., alias="unitPrice")
    promotional_price: Optional[str] = Field(None, alias="promotionalPrice")
    stock_quantity: str = Field(..., alias="stockQuantity")
    required_quantity: Optional[str] = Field(None, alias="requiredQuantity")

    class Config:
        populate_by_name = True
        alias_generator = None
        json_encoders = {}
        allow_population_by_field_name = True


class ProductNotFound(BaseModel):
    name: str
    quantity: str

    class Config:
        populate_by_name = True
        allow_population_by_field_name = True


class RecommendationResponse(BaseModel):
    products: List[ProductOut]
    not_found_products: List[ProductNotFound] = Field(..., alias="notFoundProducts")

    class Config:
        populate_by_name = True
        allow_population_by_field_name = True
