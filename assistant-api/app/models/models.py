from sqlalchemy import Column, Float, Integer, String, Boolean, Enum
from database.database import Base
import enum


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
