from database.database import get_db
from fastapi import APIRouter, Depends
from pydantic import BaseModel
from schemas import PromocaoNaoEncontrada, PromocaoOut, PromocaoRequest
from services.promocao import buscar_promocoes
from services.llm import extrair_promocoes
from sqlalchemy.orm import Session

router = APIRouter()

class PromotionsResponse(BaseModel):
    promocoes: list[PromocaoOut]
    promocoes_nao_encontradas: list[PromocaoNaoEncontrada]

@router.post("/promocao", response_model=PromotionsResponse)
async def gerar_promocoes(
    req: PromocaoRequest, db: Session = Depends(get_db)
) -> PromotionsResponse:
    print(req.promocao)
    itens_promo, promos_nao_encontradas = await extrair_promocoes(req.promocao)
    resultados = buscar_promocoes(itens_promo)
    return PromotionsResponse(
        promocoes=resultados, promocoes_nao_encontradas=promos_nao_encontradas
    )
