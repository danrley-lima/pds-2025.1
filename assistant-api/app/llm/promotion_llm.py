import tiktoken
from langchain.chat_models import init_chat_model
from langchain.prompts import ChatPromptTemplate, HumanMessagePromptTemplate
from langchain_core.messages import AIMessage

llm = init_chat_model(
    model="gemini-2.0-flash",
    temperature=0,
    max_tokens=2000,
    model_provider="google_genai",
)


def contar_tokens(texto: str) -> int:
    encoding = tiktoken.get_encoding("cl100k_base")
    return len(encoding.encode(texto))

prompt_template_promotion = ChatPromptTemplate.from_messages(
    [
        HumanMessagePromptTemplate.from_template(
            "Você é um assistente que recebe um pedido de todas as promoções ativas daquele dia de um cliente e deve retornar "
            "apenas um JSON puro e válido, sem explicações, sem texto adicional e sem marcação markdown.\n\n"
            "Aqui está a lista de promoções ativas no formato: id,description,productName,originalPrice,promotionalPrice,initialDate,finalDate:\n"
            "{promocoes_disponiveis}\n\n"
            "Com base apenas nessas promoções ativas, extraia as promoções iguais ou semelhantes aos que "
            "o cliente solicitou.\n\n"
            "Se alguma promoção que o usuário consultou não estiver disponível no banco de dados, inclua-o "
            "em uma lista separada chamada 'promocoes_nao_encontradas', informando apenas a data de hoje.\n\n"
            "Se houver mais de uma promoção ativa que atenda ao mesmo propósito (por exemplo, diferentes "
            "marcas de arroz ou produtos substitutos), escolha TODaS as promoções que atendam o pedido. "
            "Priorize a promoção de melhor reputação. Caso haja empate, escolha por ordem alfabetica.\n\n"
            "Formato esperado:\n"
            "{{\n"
            '  "promocoes": [\n'
            '    {{"id": "string", "descricao": "string", "nome_produto": "string", "preco_original": "string", "preco_promocional": "string", "data_inicial": "string", "data_final": "string"}}\n'
            "  ],\n"
            '  "promocoes_nao_encontradas": [\n'
            '    {{"data": "string"}}\n'
            "  ]\n"
            "}}\n\n"
            "Pedido do cliente: {texto}"
        ),
    ]
)


def limpar_json(texto: AIMessage) -> str:
    messagem = str(texto.content)
    return re.sub(r"^```(?:json)?\s*|\s*```$", "", messagem.strip(), flags=re.MULTILINE)

chain_promotion = prompt_template_promocao | llm | limpar_json

async def extrair_promocoes(
        texto: str,
) -> Tuple[List[PromocaoOut], list[PromocaoNaoEncontrada]]:
    promocoes = buscar_promocoes_disponiveis()
    promo_str = "; ".join(
        [
            f"{p['id']},{p['description']},{p['productName']},{p['originalPrice']},{p['promotionalPrice']},{p['initialDate']},{p['finalDate']}"
            for p in promocoes
        ]
    )

    prompt_final = prompt_template_promocao.format(
        texto=texto, promocoes_disponiveis=promo_str
    )
    num_tokens = contar_tokens(prompt_final)
    print(f"Prompt estimado em {num_tokens} tokens.")

    response = chain_promocao.invoke({"texto": texto, "promocoes_disponiveis": promo_str})

    try:
        resp_json = json.loads(response) if response else {}
    except json.JSONDecodeError as e:
        print(f"Erro ao decodificar JSON: {response} | Exception: {e}")
        return [], []
        
    print(f"Resposta do LLM: {resp_json}")

    promocoes = [
        PromocaoOut(
            id=item.get("id", ""),
            descricao=item.get("descricao", ""),
            nome_produto=item.get("nome_produto", ""),
            preco_original=item.get("preco_original", ""),
            preco_promocional=item.get("preco_promocional", ""),
            data_inicial=item.get("data_inicial", ""),
            data_final=item.get("data_final", ""),
        )
        for item in resp_json.get("promocoes", [])
        if item.get("id") and item.get("descricao") and item.get("nome_produto")
        and item.get("preco_original") and item.get("preco_promocional") and item.get("data_inicial") and item.get("data_final")
    ]

    promocoes_nao_encontradas = [
        PromocaoNaoEncontrada(**item)
        for item in resp_json.get("promocoes_nao_encontradas", [])
        if item.get("data")
    ]

    return promocoes, promocoes_nao_encontradas