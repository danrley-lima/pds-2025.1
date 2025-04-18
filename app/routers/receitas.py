from typing import List

from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from app.database.database import get_db
from app.schemas import ProdutoOut, ReceitaRequest
from app.services.ingredientes import buscar_produtos_por_ingredientes
from app.services.llm import extrair_ingredientes

router = APIRouter()


# @router.post("/receita", response_model=List[ProdutoOut])
@router.post("/receita", response_model=List[ProdutoOut])
async def gerar_carrinho(req: ReceitaRequest, db: Session = Depends(get_db)):
    # 1) usa Gemini para extrair ingredientes
    itens_ing = await extrair_ingredientes(req.receita)
    print("ingredientes extraídos", itens_ing)
    # 2) mapeia cada ingrediente em produtos (com tua lógica em services/ingredientes.py)
    resultados = buscar_produtos_por_ingredientes(itens_ing, db)
    return resultados
