# app/schemas.py
from pydantic import BaseModel


class ReceitaRequest(BaseModel):
    receita: str


class IngredienteOut(BaseModel):
    ingrediente: str
    quantidade: str


class ProdutoOut(BaseModel):
    ingrediente: str
    produto: str
    marca: str
    preco: float
    quantidade: str
