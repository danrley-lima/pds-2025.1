import json
import os
import re
from typing import List

from google import genai

from app.schemas import IngredienteOut

api_key = os.environ.get("GOOGLE_API_KEY")

client = genai.Client(api_key=api_key)


def limpar_json(texto: str) -> str:
    # Remove marcação markdown (```json ou ```), se houver
    return re.sub(r"^```(?:json)?\s*|\s*```$", "", texto.strip(), flags=re.MULTILINE)


async def extrair_ingredientes(texto: str) -> List[IngredienteOut]:
    prompt = (
        "Você é um assistente que retorna **apenas** um JSON puro e válido, "
        "sem explicações, sem texto adicional e sem marcação markdown.\n\n"
        "Formato esperado:\n"
        '{\n  "ingredientes": [\n    {"nome": "string", "quantidade": "string"}\n  ]\n}\n\n'
        f"Pedido: {texto}"
    )

    resp = client.models.generate_content(model="gemini-2.0-flash", contents=prompt)
    print(f"Resposta do Gemini: {resp.text}")
    texto_limpo = ""
    if resp.text:
        texto_limpo = limpar_json(resp.text)
    try:
        resp_json = json.loads(texto_limpo) if texto_limpo else {}
    except json.JSONDecodeError:
        print(f"Erro ao decodificar JSON: {texto_limpo}")
        return []

    ingredientes = []
    if isinstance(resp_json, dict) and "ingredientes" in resp_json:
        for item in resp_json["ingredientes"]:
            ingrediente = item.get("nome")
            quantidade = item.get("quantidade")
            if ingrediente and quantidade:
                ingredientes.append(
                    IngredienteOut(ingrediente=ingrediente, quantidade=quantidade)
                )
    return ingredientes
