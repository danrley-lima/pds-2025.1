import json
import re
from typing import List, Tuple

import tiktoken
from langchain.chat_models import init_chat_model
from langchain.prompts import ChatPromptTemplate, HumanMessagePromptTemplate
from langchain_core.messages import AIMessage
from models.models import Produto
from schemas import IngredienteOut, ProdutoNaoEncontrado
from sqlalchemy.orm import Session

_produtos_cache = None


def buscar_produtos_disponiveis(db: Session) -> list[dict]:
    global _produtos_cache
    if _produtos_cache is not None:
        return _produtos_cache
    produtos = db.query(Produto).all()
    _produtos_cache = [
        {
            "id": str(p.id),
            "nome": p.nome,
            "marca": p.marca,
            "quantidade": p.quantidade,
        }
        for p in produtos
    ]
    return _produtos_cache


llm = init_chat_model(
    model="gemini-2.0-flash",
    temperature=0,
    max_tokens=2000,
    model_provider="google_genai",
)


def contar_tokens(texto: str) -> int:
    encoding = tiktoken.get_encoding("cl100k_base")
    return len(encoding.encode(texto))


prompt_template = ChatPromptTemplate.from_messages(
    [
        HumanMessagePromptTemplate.from_template(
            "Você é um assistente que recebe um pedido de receita de um cliente e deve retornar apenas um JSON puro e válido, "
            "sem explicações, sem texto adicional e sem marcação markdown.\n\n"
            "Aqui está a lista de produtos disponíveis no formato: id,nome,marca,quantidade;:\n"
            "{produtos_disponiveis}\n\n"
            "Com base apenas nesses produtos disponíveis, extraia os produtos e quantidades necessários para a receita solicitada abaixo.\n\n"
            "Sempre converta as quantidades para valores numéricos de medida padrão (gramas, mililitros, litros, quilos, etc.), nunca utilize unidades como xícara, colher, pitada, unidade, etc.\n\n"
            "Se algum produto necessário para a receita não estiver disponível no banco de dados, inclua-o em uma lista separada chamada 'produtos_nao_encontrados', informando apenas o nome e a quantidade desejada.\n\n"
            "Se houver mais de um produto disponível que atenda ao mesmo propósito (por exemplo, diferentes marcas de arroz ou produtos substitutos), escolha apenas UM produto para cada necessidade. Priorize o produto de melhor reputação. Caso haja empate, escolha o de maior quantidade. Não repita produtos equivalentes na lista final.\n\n"
            "Se a receita não especificar detalhes adicionais, como quantidade de pessoas ou porções, considere que a receita é para 1 pessoa.\n\n"
            "Formato esperado:\n"
            "{{\n"
            '  "produtos": [\n'
            '    {{"id": "string", "quantidade": "string"}}\n'
            "  ],\n"
            '  "produtos_nao_encontrados": [\n'
            '    {{"nome": "string", "quantidade": "string"}}\n'
            "  ]\n"
            "}}\n\n"
            "Pedido do cliente: {texto}"
        ),
    ]
)


def limpar_json(texto: AIMessage) -> str:
    messagem = str(texto.content)
    return re.sub(r"^```(?:json)?\s*|\s*```$", "", messagem.strip(), flags=re.MULTILINE)


chain = prompt_template | llm | limpar_json


async def extrair_ingredientes(
    texto: str, db: Session
) -> Tuple[List[IngredienteOut], list[ProdutoNaoEncontrado]]:
    produtos = buscar_produtos_disponiveis(db)
    produtos_str = "; ".join(
        [f"{p['id']},{p['nome']},{p['marca']},{p['quantidade']}" for p in produtos]
    )

    prompt_final = prompt_template.format(
        texto=texto, produtos_disponiveis=produtos_str
    )
    num_tokens = contar_tokens(prompt_final)
    print(f"Prompt estimado em {num_tokens} tokens.")

    response = chain.invoke({"texto": texto, "produtos_disponiveis": produtos_str})

    try:
        resp_json = json.loads(response) if response else {}
    except json.JSONDecodeError as e:
        print(f"Erro ao decodificar JSON: {response} | Exception: {e}")
        return [], []

    ingredientes = [
        IngredienteOut(
            id=item.get("id", ""),
            quantidade=item.get("quantidade", ""),
        )
        for item in resp_json.get("produtos", [])
        if item.get("id") and item.get("quantidade")
    ]

    produtos_nao_encontrados = [
        ProdutoNaoEncontrado(**item)
        for item in resp_json.get("produtos_nao_encontrados", [])
        if "nome" in item and "quantidade" in item
    ]

    return ingredientes, produtos_nao_encontrados
