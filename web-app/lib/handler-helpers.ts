import { Promocao } from "./types"

export function handleError(error: unknown, context: string): never {
  console.error(`${context}:`, error)
  throw new Error(`${context}: ${error instanceof Error ? error.message : String(error)}`)
}

export async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const errorText = await response.text()
    throw new Error(errorText || `Erro HTTP: ${response.status}`)
  }
  return response.json()
}

export function parsePromocao(p: any): Promocao {
  return {
    ...p,
    dataInicial: new Date(p.dataInicial),
    dataFinal: new Date(p.dataFinal),
  }
}