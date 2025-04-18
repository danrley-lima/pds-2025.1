from sqlalchemy import Column, Float, Integer, String

from app.database.database import Base


class Produto(Base):
    __tablename__ = "produtos"
    id = Column(Integer, primary_key=True)
    nome = Column(String, unique=True)
    categoria = Column(String)
    marca = Column(String)
    preco = Column(Float)
    quantidade = Column(String)
