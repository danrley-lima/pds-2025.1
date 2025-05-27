import re
import json
import tiktoken
from fastapi import HTTPException
from typing import List, Tuple
from abc import ABC, abstractmethod
from langchain.chat_models import init_chat_model
from langchain.prompts import ChatPromptTemplate
from langchain_core.messages import AIMessage
from langchain_core.runnables import RunnableSerializable
from models.response import ProductOut, ProductNotFound
from models.request import ProductInput


class LLMHandler(ABC):
    def __init__(self):
        self.llm = self.init_llm()
        self.prompt_template = self.build_prompt_template()
        self.chain: RunnableSerializable[dict, str] = (self.prompt_template | self.llm | self.limpar_json)

    # Método template
    def search_products(self, customer_message: str, products) -> Tuple[List, List]:
        products_str = self.format_products(products)

        prompt_final = self.prompt_template.format(
            customer_message=customer_message,
            products=products_str
        )

        num_tokens = self.contar_tokens(prompt_final)
        print(f"Prompt estimado em {num_tokens} tokens.")

        try:
            response = self.chain.invoke({
                "customer_message": customer_message,
                "products": products_str
            })

            if not response:
                self.logger.warning("LLM não retornou nenhuma resposta.")
                raise HTTPException(status_code=502, detail="Falha ao obter resposta da LLM")

            return self.parse_response(response)

        except Exception as e:
            self.logger.exception(f"Erro inesperado: {str(e)}")
            raise HTTPException(status_code=500, detail="Erro interno ao processar a recomendação")
        

    def init_llm(self):
        return init_chat_model(
            model="gemini-2.0-flash",
            temperature=0,
            max_tokens=2000,
            model_provider="google_genai",
        )
    
    @abstractmethod
    def build_prompt_template(self) -> ChatPromptTemplate:
        pass

    def limpar_json(self, texto: AIMessage) -> str:
        messagem = str(texto.content)
        return re.sub(r"^```(?:json)?\s*|\s*```$", "", messagem.strip(), flags=re.MULTILINE)
    
    def contar_tokens(self, texto: str) -> int:
        encoding = tiktoken.get_encoding("cl100k_base")
        return len(encoding.encode(texto))
    
    @abstractmethod
    def format_products(self, products: List[ProductInput]) -> str:
        pass
    
    def parse_response(self, response: str) -> Tuple[List, List]:
        try:
            data = json.loads(response)
        except json.JSONDecodeError:
            self.logger.exception(f"Erro ao decodificar JSON da resposta da LLM: {response}")
            raise HTTPException(status_code=500, detail="Resposta inválida da LLM")
        
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
            for item in data.get("products", [])
            if item.get("id") and item.get("name") and item.get("brand")
            and item.get("category_name") and item.get("unit_price")
            and item.get("stock_quantity")
        ]


        not_found_products = [
            ProductNotFound(**item)
            for item in data.get("not_found_products", [])
            if item.get("name") and item.get("quantity")
        ]

        return products, not_found_products
    