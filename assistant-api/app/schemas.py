from pydantic import BaseModel


class ReceitaRequest(BaseModel):
    receita: str


class IngredienteOut(BaseModel):
    id: str
    quantidade: str

class ProdutoRequest(BaseModel):
    produto: str

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

class PromocaoRequest(BaseModel):
    promocao: str

class PromocaoOut(BaseModel):
    id: str
    descricao: str
    nome_do_produto: str
    preco_original: str
    preco_promocional: str
    data_inicial: str
    data_final: str

class PromocaoNaoEncontrada(BaseModel):
    data: str