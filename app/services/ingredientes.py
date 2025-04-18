# app/services/ingredientes.py
from typing import List
from sqlalchemy.orm import Session
from app.schemas import IngredienteOut, ProdutoOut
from app.models.models import Produto  # ajusta o import


def buscar_produtos_por_ingredientes(
    itens: List[IngredienteOut], db: Session
) -> List[ProdutoOut]:
    resultados: list[ProdutoOut] = []
    for ing in itens:
        # Exemplo: mapeia ingrediente genérico → lista de nomes de produto
        nomes = (
            db.query(Produto.nome)
            .filter(Produto.nome.ilike(f"%{ing.ingrediente}%"))
            .all()
        )
        # converte tupla pra lista simples
        nomes = [n[0] for n in nomes]
        produtos = db.query(Produto).filter(Produto.nome.in_(nomes)).all()
        for p in produtos:
            resultados.append(
                ProdutoOut(
                    ingrediente=ing.ingrediente,
                    produto=str(p.nome),
                    marca=str(p.marca),
                    preco=3,
                    # preco=float(p.preco),
                    quantidade=str(p.quantidade),
                )
            )
    return resultados
