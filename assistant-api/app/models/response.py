class CarrinhoProdutosResponse(BaseModel):
    produtos: list[ProdutoOut]
    produtos_nao_encontrados: list[ProdutoNaoEncontrado]

class CarrinhoReceitaResponse(BaseModel):
    produtos: list[ProdutoOut]
    produtos_nao_encontrados: list[ProdutoNaoEncontrado]

class PromotionsResponse(BaseModel):
    promocoes: list[PromocaoOut]
    promocoes_nao_encontradas: list[PromocaoNaoEncontrada]

class IngredienteOut(BaseModel):
    id: str
    quantidade: str

class PromocaoOut(BaseModel):
    id: str
    descricao: str
    nome_produto: str
    preco_original: str
    preco_promocional: str
    data_inicial: str
    data_final: str


class ProductOut(BaseModel):
    id: str
    nome: str
    marca: str
    preco: str
    quantidade_total: str
    embalagens_necessarias: str


class ProdutoNaoEncontrado(BaseModel):
    nome: str
    quantidade: str



class PromocaoNaoEncontrada(BaseModel):
    data: str