import enum

from database.database import Base
from sqlalchemy import Boolean, Column, Enum, Float, Integer, String


class UnitTypeEnum(str, enum.Enum):
    G = "G"
    KG = "KG"
    ML = "ML"
    L = "L"
    UN = "UN"


class Product(Base):
    __tablename__ = "products"
    id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String, nullable=False)
    brand = Column(String, nullable=True)
    unit_weight = Column(Float, nullable=True)  # peso por unidade
    unit_type = Column(Enum(UnitTypeEnum), nullable=True)  # tipo da unidade
    stock_quantity = Column(Integer, nullable=True)  # unidades em estoque
    unit_price = Column(Float, nullable=True)
    available = Column(Boolean, default=True)

class Promotion(Base):
    __tablename__ = "promotions"
    id = Column(Integer, primary_key=True, autoincrement=True)
    description = Column(String, nullable=True)
    product_name = Column(String, nullable=False)
    original_price = Column(Float, nullable=False)
    promotional_price = Column(Float, nullable=False)
    initial_date = Column(Date, nullable=False)
    final_date = Column(Date, nullable=False)