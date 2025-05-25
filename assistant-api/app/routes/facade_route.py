from database.database import get_db
from fastapi import APIRouter, Depends
from services.produtos import buscar_produtos
from services.llm import extrair_produtos
from sqlalchemy.orm import Session

router = APIRouter()

@router.post("/recommendations", response_model=CarrinhoProdutosResponse)
async def gerar_carrinho_produtos(
    req: ProdutoRequest, db: Session = Depends(get_db)
) -> CarrinhoProdutosResponse:
    print(req.produto)
    itens_prod, produtos_nao_encontrados = await extrair_produtos(req.produto)
    resultados = buscar_produtos(itens_prod)
    return CarrinhoProdutosResponse(
        produtos=resultados, produtos_nao_encontrados=produtos_nao_encontrados
    )


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


@router.post("/receita", response_model=CarrinhoReceitaResponse)
async def gerar_carrinho(
    req: ReceitaRequest, db: Session = Depends(get_db)
) -> CarrinhoReceitaResponse:
    """
    Gera um carrinho de compras a partir de uma receita, retornando produtos encontrados e não encontrados.
    """
    itens_ing, produtos_nao_encontrados = await extrair_ingredientes(req.receita)
    resultados = buscar_produtos_por_ingredientes(itens_ing)
    return CarrinhoReceitaResponse(
        produtos=resultados, produtos_nao_encontrados=produtos_nao_encontrados
    )
