from database.database import get_db
from fastapi import APIRouter, Depends
from pydantic import BaseModel
from schemas import ProdutoNaoEncontrado, ProdutoOut, ReceitaRequest
from services.ingredientes import buscar_produtos_por_ingredientes
from services.llm import extrair_ingredientes
from sqlalchemy.orm import Session

router = APIRouter()


class CarrinhoResponse(BaseModel):
    produtos: list[ProdutoOut]
    produtos_nao_encontrados: list[ProdutoNaoEncontrado]


@router.post("/receita", response_model=CarrinhoResponse)
async def gerar_carrinho(
    req: ReceitaRequest, db: Session = Depends(get_db)
) -> CarrinhoResponse:
    """
    Gera um carrinho de compras a partir de uma receita, retornando produtos encontrados e não encontrados.
    """
    itens_ing, produtos_nao_encontrados = await extrair_ingredientes(req.receita, db)
    resultados = buscar_produtos_por_ingredientes(itens_ing, db)
    return CarrinhoResponse(
        produtos=resultados, produtos_nao_encontrados=produtos_nao_encontrados
    )
