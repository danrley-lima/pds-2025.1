import logging
from typing import List
from langchain.prompts import ChatPromptTemplate, HumanMessagePromptTemplate
from llm.abstract_llm_handler import LLMHandler
from models.request import ProductInput




class ProductLLMHandler(LLMHandler):
    def __init__(self):
        self.logger = logging.getLogger(__name__)
        super().__init__()

    def format_products(self, products: List[ProductInput]) -> str:
        return "; ".join([
            f"{p.id},{p.name},{p.brand},{p.category_name},{p.unit_weight} {p.unit_type},{p.unit_price},{p.available},{p.on_promotion},{p.promotional_price},{p.stock_quantity},{p.priority}"
            for p in products
        ])

    def build_prompt_template(self) -> ChatPromptTemplate:
        return ChatPromptTemplate.from_messages(
            [
                HumanMessagePromptTemplate.from_template(
                    "Você é um assistente que recebe um pedido de um produto de um cliente e deve retornar "
                    "apenas um JSON puro e válido, sem explicações, sem texto adicional e sem marcação markdown.\n\n"
                    "Aqui está a lista de produtos disponíveis no formato: id,name,brand,category_name,unit_weight unit_type,"
                    "unit_price,available,on_promotion,promotional_price,stock_quantity,priority;:"
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
                    "são os produtos em promoção, em segundo lugar são os produtos com priority == true, e por último os "
                    "produtos comuns.\n\n"
                    "Formato esperado:\n"
                    "{{\n"
                    '  "products": [\n'
                    '   {{"id": "string", "name": "string", "brand": "string", "category_name": "string", \
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
