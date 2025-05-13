from database.database import get_db
from fastapi import APIRouter, Depends
from pydantic import BaseModel
from schemas import ProdutoNaoEncontrado, ProdutoOut, ProdutoRequest
from services.produtos import buscar_produtos
from services.llm import extrair_produtos
from sqlalchemy.orm import Session

router = APIRouter()

class CarrinhoProdutosResponse(BaseModel):
    produtos: list[ProdutoOut]
    produtos_nao_encontrados: list[ProdutoNaoEncontrado]

@router.post("/produto", response_model=CarrinhoProdutosResponse)
async def gerar_carrinho_produtos(
    req: ProdutoRequest, db: Session = Depends(get_db)
) -> CarrinhoProdutosResponse:
    print(req.produto)
    itens_prod, produtos_nao_encontrados = await extrair_produtos(req.produto)
    resultados = buscar_produtos(itens_prod)
    return CarrinhoProdutosResponse(
        produtos=resultados, produtos_nao_encontrados=produtos_nao_encontrados
    )
