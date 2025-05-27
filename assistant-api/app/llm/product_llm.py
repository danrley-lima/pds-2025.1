import tiktoken
import re
import json
import logging
from fastapi import HTTPException
from typing import List, Tuple
from langchain.chat_models import init_chat_model
from langchain.prompts import ChatPromptTemplate, HumanMessagePromptTemplate
from langchain_core.messages import AIMessage
from models.response import ProductOut, ProductNotFound
from models.request import ProductInput, PromotionInput


logger = logging.getLogger(__name__)

llm = init_chat_model(
    model="gemini-2.0-flash",
    temperature=0,
    max_tokens=2000,
    model_provider="google_genai",
)

def contar_tokens(texto: str) -> int:
    encoding = tiktoken.get_encoding("cl100k_base")
    return len(encoding.encode(texto))

def limpar_json(texto: AIMessage) -> str:
    messagem = str(texto.content)
    return re.sub(r"^```(?:json)?\s*|\s*```$", "", messagem.strip(), flags=re.MULTILINE)

prompt_template_product = ChatPromptTemplate.from_messages(
    [
        HumanMessagePromptTemplate.from_template(
            "Você é um assistente que recebe um pedido de um produto de um cliente e deve retornar "
            "apenas um JSON puro e válido, sem explicações, sem texto adicional e sem marcação markdown.\n\n"
            "Aqui está a lista de produtos disponíveis no formato: id,name,brand,category_name,unit_weight unit_type,unit_price,stock_quantity,priority; "
            " ou quando houver promoção, ele ficará no formata: id,name,brand,category_name,unit_weight unit_type,unit_price,stock_quantity,priority,PROMO:promotional_price;:\n"
            "{products}\n\n"
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
            "Sobre a ordem dos produtos, temos 3 categorias que você deve priorizar, lembrando de respeitar "
            "e retornar produtos correspondentes ao pedido do cliente e que estajam em estoque: A maior prioridade "
            "são os produtos em promoção, em segundo lugar são os produtos com priority == true, e por último os produtos comuns.\n\n"
            "Formato esperado:\n"
            "{{\n"
            '  "products": [\n'
            '    {{"id": "string", "name": "string", "brand": "string", "category_name": "string", "unit_price": "string", "promotional_price": "string", "stock_quantity": "string", "required_quantity": "string"}}\n'
            "  ],\n"
            '  "not_found_products": [\n'
            '    {{"name": "string", "quantity": "string"}}\n'
            "  ]\n"
            "}}\n\n"
            "Pedido do cliente: {customer_message}"
        ),
    ]
)

chain_product = prompt_template_product | llm | limpar_json

def search_product_llm(
    customer_message: str,
    products: List[ProductInput],
    promotions: List[PromotionInput]
) -> Tuple[List[ProductOut], List[ProductNotFound]]:
    promotions_dict = {p.product_id: p for p in promotions}

    products_str = "; ".join(
        [
            f"{p.id},{p.name},{p.brand},{p.category_name},{p.unit_weight} {p.unit_type},{p.unit_price},{p.stock_quantity},{p.priority}"
            + (
                f",PROMO:{promotions_dict[p.id].promotional_price}"
                if p.id in promotions_dict else ""
            )
            for p in products
        ]
    )

    prompt_final = prompt_template_product.format(
        customer_message=customer_message, products=products_str
    )
    num_tokens = contar_tokens(prompt_final)
    print(f"Prompt estimado em {num_tokens} tokens.")


    try:
        response = chain_product.invoke({
            "customer_message": customer_message,
            "products": products_str
        })
        if not response:
            logger.warning("LLM não retornou nenhuma resposta.")
            raise HTTPException(status_code=502, detail="Falha ao obter resposta da LLM")

        resp_json = json.loads(response)

    except json.JSONDecodeError as e:
        logger.exception(f"Erro ao decodificar JSON da resposta da LLM: {response}")
        raise HTTPException(status_code=500, detail="Resposta inválida da LLM")

    except Exception as e:
        logger.exception(f"Erro inesperado ao chamar a LLM: {str(e)}")
        raise HTTPException(status_code=500, detail="Erro interno ao processar a recomendação")
    
    products = [
        ProductOut(
            id=item.get("id", ""),
            name=item.get("name", ""),
            brand=item.get("brand", ""),
            category_name=item.get("category_name", ""),
            unit_price=item.get("unit_price", ""),
            promotional_price=item.get("promotional_price", ""),
            stock_quantity=item.get("stock_quantity", ""),
            required_quantity=item.get("required_quantity", ""),
        )
        for item in resp_json.get("products", [])
        if item.get("id") and item.get("name") and item.get("brand")
        and item.get("category_name") and item.get("unit_price")
        and item.get("stock_quantity")
    ]


    not_found_products = [
        ProductNotFound(**item)
        for item in resp_json.get("not_found_products", [])
        if item.get("name") and item.get("quantity")
    ]

    return products, not_found_products
