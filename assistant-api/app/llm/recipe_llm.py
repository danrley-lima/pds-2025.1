import logging
from typing import List
from langchain.prompts import ChatPromptTemplate, HumanMessagePromptTemplate
from llm.abstract_llm_handler import LLMHandler
from models.request import ProductInput, PromotionInput


class RecipeLLMHandler(LLMHandler):
    def __init__(self):
        self.logger = logging.getLogger(__name__)
        super().__init__()

    def format_products(self, products: List[ProductInput], promotions: List[PromotionInput]) -> str:
        promotions_dict = {p.product_id: p for p in promotions}
        return "; ".join([
            f"{p.id},{p.name},{p.brand},{p.category_name},{p.unit_weight} {p.unit_type},{p.unit_price},{p.stock_quantity},{p.priority}" +
            (f",PROMO:{promotions_dict[p.id].description},{promotions_dict[p.id].promotional_price},{promotions_dict[p.id].initial_date},{promotions_dict[p.id].final_date}" if p.id in promotions_dict else "")
            for p in products
        ])

    def build_prompt_template(self) -> ChatPromptTemplate:
        return ChatPromptTemplate.from_messages(
            [
                HumanMessagePromptTemplate.from_template(
                    "Você é um assistente que recebe um pedido de receita de um cliente e deve retornar "
                    "apenas um JSON puro e válido, sem explicações, sem texto adicional e sem "
                    "marcação markdown.\n\n"
                    "Aqui está a lista de produtos disponíveis no formato: id,name,brand,category_name,"
                    "unit_weight unit_type,unit_price,stock_quantity,priority; "
                    "Ou quando houver promoção, ele ficará no formata: id,name,brand,category_name,"
                    "unit_weight unit_type,unit_price,stock_quantity,priority,PROMO:description,"
                    "promotional_price,initial_date,final_date;:\n"
                    "{products}\n\n"
                    "Com base apenas nesses produtos disponíveis, extraia os produtos e quantidades necessários "
                    "para a receita solicitada abaixo.\n\n"
                    "Sempre converta as quantidades para valores numéricos de medida padrão (gramas, mililitros, "
                    "litros, quilos, etc.), "
                    "e SEMPRE informe a unidade junto ao valor, por exemplo: '200g', '2L', '500ml', '1kg'. Nunca "
                    "retorne apenas o número.\n\n"
                    "Se algum produto necessário para a receita não estiver disponível no banco de dados, "
                    "inclua-o em uma lista separada chamada 'produtos_nao_encontrados', informando apenas "
                    "o nome e a quantidade desejada, também com unidade.\n\n"
                    "Se houver mais de um produto disponível que atenda ao mesmo propósito (por exemplo, diferentes "
                    "marcas de arroz ou produtos substitutos), escolha apenas UM produto para cada necessidade. "
                    "Priorize primeiro os produtos em promoção, depois aqueles marcados com priority==true, "
                    "e por ultimo os produtos normais. Caso haja empate, escolha o de maior quantidade. Não "
                    "repita produtos equivalentes na lista final.\n\n"
                    "Se a receita não especificar detalhes adicionais, como quantidade de pessoas ou porções, "
                    "considere que a receita é para 1 pessoa.\n\n"
                    "Formato esperado:\n"
                    "{{\n"
                    '  "products": [\n'
                    '    {{"id": "string", "name": "string", "brand": "string", "category_name": "string", \
                            "unit_price": "string", "promotional_price": "string", "stock_quantity": "string", \
                            "required_quantity": "string"}}\n'
                    "  ],\n"
                    '  "not_found_products": [\n'
                    '    {{"name": "string", "quantity": "string"}}\n'
                    "  ]\n"
                    "}}\n\n"
                    "Pedido do cliente: {customer_message}"
                ),
            ]
        )
