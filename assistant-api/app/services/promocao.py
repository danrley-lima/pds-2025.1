import re
from typing import List

from services.llm import buscar_promocoes_disponiveis
from schemas import PromocaoOut

def buscar_promocoes(itens: List[PromocaoOut]) -> List[PromocaoOut]:
    ids = [str(ing.id) for ing in itens if ing.id is not None]
    if not ids:
        return []

    promos = buscar_promocoes_disponiveis()
    promo_dict = {str(p["id"]): p for p in promos if str(p["id"]) in ids}

    resultado = []
    for promotion in itens:
        promocao = promo_dict.get(str(promotion.id))
        if not promocao:
            continue
        
        resultado.append(
            PromocaoOut(
                id=str(promocao["id"]),
                descricao=str(promocao.get("description", "")),
                nome_do_produto=str(promocao.get("productName", "")),
                preco_original=str(promocao.get("promotionalPrice", "")),
                preco_promocional=str(promocao.get("originalPrice", "")),
                data_inicial=str(promocao.get("initialDate", "")),
                data_final=str(promocao.get("finalDate", "")),
            )
        )
    return resultado
