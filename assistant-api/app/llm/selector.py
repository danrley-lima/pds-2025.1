import tiktoken
from langchain.chat_models import init_chat_model
from langchain.prompts import ChatPromptTemplate, HumanMessagePromptTemplate


llm = init_chat_model(
    model="gemini-2.0-flash",
    temperature=0,
    max_tokens=2000,
    model_provider="google_genai",
)


def contar_tokens(texto: str) -> int:
    encoding = tiktoken.get_encoding("cl100k_base")
    return len(encoding.encode(texto))


prompt_template_selector = ChatPromptTemplate.from_messages(
    [
        HumanMessagePromptTemplate.from_template(
            "Você é um assistente que recebe um pedido de um cliente e deve retornar"
            "apenas uma string referente ao que o cliente quer sem explicações, sem texto adicional e sem "
            "marcação markdown.\n\n"
            "Você deve classificar o pedido em 3 categorias. A primeira é uma busca de produto, você deve "
            "retornar ela quando o cliente está buscando algum produto específico ou uma categoria de produto, "
            "podendo ser um ou mais produtos ou categorias. Se o pedido do cliente se tratar disso, você "
            "deve retornar a string \"searchProduct\".\n"
            "A segunda é uma busca de promoções de produtos ou categorias. Se o pedido do cliente se trarar disso, "
            "você deve retornar a string \"searchPromotion\".\n"
            "A terceira é uma busca de alguma receita, uma pergunta de como fazer uma receita, se o pedido do cliente "
            "se tratar disso, você deve retornar a string \"recipe\".\n\n"
            "O pedido do cliente foi o seguinte: {customer_message}\n\n"
            "Caso o pedido do cliente não se encaixe em nenhuma dessas categorias, "
            "você deve retornar a string \"unknown\".\n\n"
        ),
    ]
)


chain_selector = prompt_template_selector | llm

def classify_text(
        customer_message: str,
) -> str:

    prompt_final = prompt_template_selector.format(
        customer_message=customer_message
    )
    num_tokens = contar_tokens(prompt_final)
    print(f"Prompt estimado em {num_tokens} tokens.")

    response = chain_selector.invoke({"customer_message": customer_message}).content.strip()

    return response