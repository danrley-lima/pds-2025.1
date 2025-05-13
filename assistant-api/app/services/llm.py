import json
import re
import time
from typing import List, Tuple

import requests
import tiktoken
from langchain.chat_models import init_chat_model
from langchain.prompts import ChatPromptTemplate, HumanMessagePromptTemplate
from langchain_core.messages import AIMessage
from schemas import IngredienteOut, ProdutoOut, ProdutoNaoEncontrado

_produtos_cache = None
_produtos_cache_timestamp = None
CACHE_EXPIRATION_SECONDS = 60  # Expira após 1 minutos


def buscar_produtos_disponiveis() -> list[dict]:
    global _produtos_cache, _produtos_cache_timestamp
    current_time = time.time()
    if (
        _produtos_cache is not None
        and _produtos_cache_timestamp is not None
        and (current_time - _produtos_cache_timestamp) < CACHE_EXPIRATION_SECONDS
    ):
        return _produtos_cache
    response = requests.get("http://localhost:3000/api/products")
    response.raise_for_status()
    _produtos_cache = response.json()
    _produtos_cache_timestamp = current_time
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
            "Você é um assistente que recebe um pedido de receita de um cliente e deve retornar "
            "apenas um JSON puro e válido, sem explicações, sem texto adicional e sem marcação markdown.\n\n"
            "Aqui está a lista de produtos disponíveis no formato: id,nome,marca,quantidade;:\n"
            "{produtos_disponiveis}\n\n"
            "Com base apenas nesses produtos disponíveis, extraia os produtos e quantidades necessários "
            "para a receita solicitada abaixo.\n\n"
            "Sempre converta as quantidades para valores numéricos de medida padrão (gramas, mililitros, "
            "litros, quilos, etc.), "
            "e SEMPRE informe a unidade junto ao valor, por exemplo: '200g', '2L', '500ml', '1kg'. Nunca "
            "retorne apenas o número.\n\n"
            "Se algum produto necessário para a receita não estiver disponível no banco de dados, inclua-o "
            "em uma lista separada chamada 'produtos_nao_encontrados', informando apenas o nome e a quantidade "
            "desejada, também com unidade.\n\n"
            "Se houver mais de um produto disponível que atenda ao mesmo propósito (por exemplo, diferentes "
            "marcas de arroz ou produtos substitutos), escolha apenas UM produto para cada necessidade. "
            "Priorize o produto de melhor reputação. Caso haja empate, escolha o de maior quantidade. Não "
            "repita produtos equivalentes na lista final.\n\n"
            "Se a receita não especificar detalhes adicionais, como quantidade de pessoas ou porções, "
            "considere que a receita é para 1 pessoa.\n\n"
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

prompt_template_produto = ChatPromptTemplate.from_messages(
    [
        HumanMessagePromptTemplate.from_template(
            "Você é um assistente que recebe um pedido de um produto de um cliente e deve retornar "
            "apenas um JSON puro e válido, sem explicações, sem texto adicional e sem marcação markdown.\n\n"
            "Aqui está a lista de produtos disponíveis no formato: id,nome,marca,quantidade;:\n"
            "{produtos_disponiveis}\n\n"
            "Com base apenas nesses produtos disponíveis, extraia os produtos iguais ou semelhantes aos que "
            "o cliente solicitou.\n\n"
            "Sempre converta as quantidades para valores numéricos de medida padrão (gramas, mililitros, "
            "litros, quilos, etc.), "
            "e SEMPRE informe a unidade junto ao valor, por exemplo: '200g', '2L', '500ml', '1kg'. Nunca "
            "retorne apenas o número.\n\n"
            "Se algum produto que o usuário pediu não estiver disponível no banco de dados, inclua-o "
            "em uma lista separada chamada 'produtos_nao_encontrados', informando apenas o nome e a quantidade "
            "desejada, também com unidade.\n\n"
            "Se houver mais de um produto disponível que atenda ao mesmo propósito (por exemplo, diferentes "
            "marcas de arroz ou produtos substitutos), escolha TODOS os produtos que atendam o pedido. "
            "Priorize o produto de melhor reputação. Caso haja empate, escolha o de maior quantidade. Não "
            "repita produtos equivalentes na lista final.\n\n"
            "Formato esperado:\n"
            "{{\n"
            '  "produtos": [\n'
            '    {{"id": "string", "nome": "string", "marca": "string", "preco: "string", "quantidade_total": "string", "embalagens_necessarias": "string"}}\n'
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

chain_produto = prompt_template_produto | llm | limpar_json

async def extrair_produtos(
        texto: str,
) -> Tuple[List[IngredienteOut], list[ProdutoNaoEncontrado]]:
    produtos = buscar_produtos_disponiveis()
    produtos_str = "; ".join(
        [
            f"{p['id']},{p['name']},{p['brand']},{p['unitWeight']} {p['unitType']},{p['unitPrice']}"
            for p in produtos
        ]
    )
    prompt_final = prompt_template_produto.format(
        texto=texto, produtos_disponiveis=produtos_str
    )
    num_tokens = contar_tokens(prompt_final)
    print(f"Prompt estimado em {num_tokens} tokens.")

    response = chain_produto.invoke({"texto": texto, "produtos_disponiveis": produtos_str})

    try:
        resp_json = json.loads(response) if response else {}
    except json.JSONDecodeError as e:
        print(f"Erro ao decodificar JSON: {response} | Exception: {e}")
        return [], []
    
    produtos = [
        ProdutoOut(
            id=item.get("id", ""),
            nome=item.get("nome", ""),
            marca=item.get("marca", ""),
            preco=item.get("preco", ""),
            quantidade_total=item.get("quantidade_total", ""),
            embalagens_necessarias="",
        )
        for item in resp_json.get("produtos", [])
        if item.get("id") and item.get("nome") and item.get("marca")
        and item.get("preco") and item.get("quantidade_total")
    ]

    produtos_nao_encontrados = [
        ProdutoNaoEncontrado(**item)
        for item in resp_json.get("produtos_nao_encontrados", [])
        if item.get("nome") and item.get("quantidade")
    ]

    return produtos, produtos_nao_encontrados


async def extrair_ingredientes(
    texto: str,
) -> Tuple[List[IngredienteOut], list[ProdutoNaoEncontrado]]:
    produtos = buscar_produtos_disponiveis()
    produtos_str = "; ".join(
        [
            f"{p['id']},{p['name']},{p['brand']},{p['unitWeight']} {p['unitType']},{p['unitPrice']}"
            for p in produtos
        ]
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
