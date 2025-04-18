import re
from typing import List

from models.models import Product
from schemas import IngredienteOut, ProdutoOut
from sqlalchemy.orm import Session


_produtos_cache = None


def get_produtos_cache(db: Session) -> list:
    global _produtos_cache
    if _produtos_cache is not None:
        return _produtos_cache
    _produtos_cache = db.query(Product).all()
    return _produtos_cache


def parse_quantidade(qtd: str) -> float:
    """
    Converte uma string de quantidade para um valor numérico em gramas ou mililitros.
    Exemplo: "500g" -> 500, "1kg" -> 1000, "2L" -> 2000, "500ml" -> 500
    """
    qtd = qtd.replace(",", ".").strip().lower()
    match = re.match(r"([\d\.]+)\s*(kg|g|l|ml)", qtd)
    if not match:
        return 0
    valor, unidade = match.groups()
    valor = float(valor)
    if unidade == "kg":
        return valor * 1000
    if unidade == "g":
        return valor
    if unidade == "l":
        return valor * 1000
    if unidade == "ml":
        return valor
    return 0


def buscar_produtos_por_ingredientes(
    itens: List[IngredienteOut], db: Session
) -> List[ProdutoOut]:
    ids = [int(ing.id) for ing in itens if ing.id is not None]
    if not ids:
        return []

    # produtos = db.query(Produto).filter(Produto.id.in_(ids)).all()
    # produtos_dict = {str(p.id): p for p in produtos}
    produtos = get_produtos_cache(db)
    produtos_dict = {str(p.id): p for p in produtos if str(p.id) in map(str, ids)}

    resultado = []
    for ing in itens:
        produto = produtos_dict.get(ing.id)
        if not produto:
            continue
        qtd_receita = parse_quantidade(ing.quantidade)
        qtd_embalagem = parse_quantidade(str(produto.quantidade))
        if qtd_embalagem == 0:
            unidades = 1
        else:
            unidades = int(-(-qtd_receita // qtd_embalagem))  # Arredondamento para cima
        resultado.append(
            ProdutoOut(
                id=str(produto.id),
                produto=str(produto.nome),
                marca=str(produto.marca),
                preco=str(produto.preco),
                quantidade_total=ing.quantidade,
                embalagens_necessarias=f"{unidades} unidade(s) de {produto.quantidade}",
            )
        )
    return resultado
