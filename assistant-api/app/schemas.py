from pydantic import BaseModel


class ReceitaRequest(BaseModel):
    receita: str


class IngredienteOut(BaseModel):
    id: str
    quantidade: str


class ProdutoOut(BaseModel):
    id: str
    nome: str
    marca: str
    preco: str
    quantidade_total: str
    embalagens_necessarias: str


class ProdutoNaoEncontrado(BaseModel):
    nome: str
    quantidade: str
