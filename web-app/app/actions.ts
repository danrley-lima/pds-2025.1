"use server"

import { generateText } from "ai"
import { openai } from "@ai-sdk/openai"
import { revalidatePath } from "next/cache"
import { salvarHistoricoPesquisa } from "@/lib/api"

export async function processarSolicitacaoCompra(consulta: string) {
  try {
    // Salva a consulta no histórico
    await salvarHistoricoPesquisa(consulta)

    // Processa a consulta com IA para entender a solicitação
    const { text: respostaIA } = await generateText({
      model: openai("gpt-4o"),
      prompt: `
        Você é um assistente de IA para um aplicativo de compras de supermercado.
        Analise a seguinte solicitação do usuário e extraia:
        1. O tipo de prato ou refeição que desejam fazer
        2. O número de porções ou pessoas
        3. Quaisquer restrições ou preferências alimentares
        
        Solicitação do usuário: "${consulta}"
        
        Responda em formato JSON com a seguinte estrutura:
        {
          "tipoPrato": "string",
          "porcoes": number,
          "restricoesAlimentares": ["string"],
          "observacoesAdicionais": "string"
        }
      `,
    })

    // Em um app real, você usaria esses dados estruturados para consultar seu banco de dados de produtos
    console.log("Solicitação processada pela IA:", respostaIA)

    // Revalida a página de resultados para mostrar dados atualizados
    revalidatePath("/resultados")

    return { success: true }
  } catch (error) {
    console.error("Erro ao processar solicitação de compras:", error)
    return { success: false, error: "Falha ao processar solicitação" }
  }
}
