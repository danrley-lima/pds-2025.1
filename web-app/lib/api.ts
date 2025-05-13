import { handleError, handleResponse, parsePromocao } from "./handler-helpers"
import { Promocao } from "./types"

const API_BASE_URL = "http://localhost:3000/api"

async function get<T>(endpoint: string, headers?: HeadersInit): Promise<T> {
  const response = await fetch(${API_BASE_URL}${endpoint}, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      ...headers,
    },
  })
  return handleResponse<T>(response)
}

async function post<T, Body = any>(endpoint: string, body: Body, headers?: HeadersInit): Promise<T> {
  const response = await fetch(${API_BASE_URL}${endpoint}, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      ...headers,
    },
    body: JSON.stringify(body),
  })
  return handleResponse<T>(response)
}

async function put<T, Body = any>(endpoint: string, body: Body, headers?: HeadersInit): Promise<T> {
  const response = await fetch(${API_BASE_URL}${endpoint}, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      ...headers,
    },
    body: JSON.stringify(body),
  })
  return handleResponse<T>(response)
}

async function del<T>(endpoint: string, headers?: HeadersInit): Promise<T> {
  const response = await fetch(${API_BASE_URL}${endpoint}, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      ...headers,
    },
  })
  return handleResponse<T>(response)
}

export const promotionsApi = {
  async listar(): Promise<Promocao[]> {
    try {
      const data = await get<any[]>("/promotions")
      return data.map(parsePromocao)
    } catch (error) {
      handleError(error, "Erro ao listar promoções")
    }
  },

  async criar(data: Omit<Promocao, "id">): Promise<Promocao> {
    try {
      const result = await post<Promocao>("/promotions", data)
      return parsePromocao(result)
    } catch (error) {
      handleError(error, "Erro ao criar promoção")
    }
  },

  async atualizar(id: string, data: Partial<Omit<Promocao, "id">>): Promise<Promocao> {
    try {
      const result = await put<Promocao>(/promotions/${id}, data)
      return parsePromocao(result)
    } catch (error) {
      handleError(error, "Erro ao atualizar promoção")
    }
  },

  async excluir(id: string): Promise<void> {
    try {
      await del(/promotions/${id})
    } catch (error) {
      handleError(error, "Erro ao excluir promoção")
    }
  },
}