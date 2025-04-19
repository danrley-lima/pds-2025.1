import re
from typing import List

from llm import buscar_produtos_disponiveis
from schemas import IngredienteOut, ProdutoOut

_produtos_cache = None


# def get_produtos_cache() -> list:
#     global _produtos_cache
#     if _produtos_cache is not None:
#         return _produtos_cache
#     response = requests.get("http://localhost:3000/api/products")
#     response.raise_for_status()
#     _produtos_cache = response.json()
#     return _produtos_cache


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


def buscar_produtos_por_ingredientes(itens: List[IngredienteOut]) -> List[ProdutoOut]:
    ids = [str(ing.id) for ing in itens if ing.id is not None]
    if not ids:
        return []

    produtos = buscar_produtos_disponiveis()
    produtos_dict = {str(p["id"]): p for p in produtos if str(p["id"]) in ids}

    resultado = []
    for ing in itens:
        produto = produtos_dict.get(str(ing.id))
        if not produto:
            continue
        qtd_receita = parse_quantidade(ing.quantidade)
        # Monta a quantidade do produto no formato "100g", "1kg", etc.
        unit_weight = produto.get("unitWeight", "")
        unit_type = produto.get("unitType", "")
        quantidade_produto = (
            f"{unit_weight}{unit_type}".lower() if unit_weight and unit_type else ""
        )
        qtd_embalagem = parse_quantidade(quantidade_produto)
        if qtd_embalagem == 0:
            unidades = 1
        else:
            unidades = int(-(-qtd_receita // qtd_embalagem))  # Arredondamento para cima
        resultado.append(
            ProdutoOut(
                id=str(produto["id"]),
                nome=str(produto.get("name", "")),
                marca=str(produto.get("brand", "")),
                preco=str(produto.get("unitPrice", "")),
                quantidade_total=ing.quantidade,
                embalagens_necessarias=f"{unidades} unidade(s) de {quantidade_produto}",
            )
        )
    return resultado
