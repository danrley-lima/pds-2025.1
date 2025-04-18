import type { Receita, Produto } from "./types"

// Em um app real, estas seriam chamadas reais para sua API
export async function obterResultadosReceitas(consulta: string): Promise<Receita[]> {
  // Simula atraso de chamada de API
  await new Promise((resolve) => setTimeout(resolve, 1000))

  // Dados de exemplo - em um app real, isso viria da sua API
  if (consulta.toLowerCase().includes("lasanha")) {
    return [
      {
        id: "1",
        titulo: "Lasanha de Carne Clássica",
        descricao: "Uma lasanha italiana tradicional com molho de carne rico, bechamel cremoso e camadas de massa.",
        imagem: "/placeholder.svg?height=300&width=300",
        tempoPreparo: "1 hora e 30 mins",
        porcoes: 10,
        dificuldade: "Médio",
      },
      {
        id: "2",
        titulo: "Lasanha Vegetariana de Espinafre",
        descricao: "Uma alternativa sem carne com ricota, espinafre e um saboroso molho de tomate.",
        imagem: "/placeholder.svg?height=300&width=300",
        tempoPreparo: "1 hora e 15 mins",
        porcoes: 10,
        dificuldade: "Fácil",
      },
    ]
  }

  // Dados de exemplo padrão para outras consultas
  return [
    {
      id: "3",
      titulo: "Exemplo de Resultado de Receita",
      descricao: "Esta é uma receita de exemplo para fins de demonstração.",
      imagem: "/placeholder.svg?height=300&width=300",
      tempoPreparo: "45 mins",
      porcoes: 4,
      dificuldade: "Médio",
    },
  ]
}

export async function obterListaCompras(consulta: string): Promise<Produto[]> {
  // Simula atraso de chamada de API
  await new Promise((resolve) => setTimeout(resolve, 800))

  // Dados de exemplo - em um app real, isso viria da sua API
  if (consulta.toLowerCase().includes("lasanha")) {
    return [
      { id: "1", nome: "Carne Moída", preco: 25.99, marca: "Carnes Premium", categoria: "Carnes", quantidade: "500g" },
      { id: "2", nome: "Massa para Lasanha", preco: 8.49, marca: "Barilla", categoria: "Massas", quantidade: "500g" },
      { id: "3", nome: "Queijo Ricota", preco: 12.99, marca: "Vigor", categoria: "Laticínios", quantidade: "250g" },
      { id: "4", nome: "Queijo Mussarela", preco: 9.99, marca: "Tirolez", categoria: "Laticínios", quantidade: "400g" },
      { id: "5", nome: "Molho de Tomate", preco: 5.49, marca: "Quero", categoria: "Molhos", quantidade: "340g" },
      { id: "6", nome: "Cebolas", preco: 3.29, marca: "Hortifruti", categoria: "Hortifruti", quantidade: "500g" },
      { id: "7", nome: "Alho", preco: 2.99, marca: "Hortifruti", categoria: "Hortifruti", quantidade: "100g" },
    ]
  }

  // Dados de exemplo padrão para outras consultas
  return [
    {
      id: "8",
      nome: "Produto de Exemplo 1",
      preco: 15.99,
      marca: "Marca A",
      categoria: "Diversos",
      quantidade: "1 un",
    },
    { id: "9", nome: "Produto de Exemplo 2", preco: 8.99, marca: "Marca B", categoria: "Diversos", quantidade: "500g" },
    {
      id: "10",
      nome: "Produto de Exemplo 3",
      preco: 5.49,
      marca: "Marca C",
      categoria: "Diversos",
      quantidade: "250ml",
    },
  ]
}

export async function salvarHistoricoPesquisa(consulta: string) {
  // Em um app real, isso salvaria em um banco de dados
  console.log("Salvando histórico de pesquisa:", consulta)
  return { success: true }
}

// API para o CRUD de produtos
export async function listarProdutos(): Promise<Produto[]> {
  try {
    const response = await fetch("http://sua-api.com/produtos", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })

    if (!response.ok) {
      throw new Error("Falha ao buscar produtos")
    }

    return await response.json()
  } catch (error) {
    console.error("Erro ao listar produtos:", error)
    return []
  }
}

export async function obterProduto(id: string): Promise<Produto | null> {
  try {
    const response = await fetch(`http://sua-api.com/produtos/${id}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })

    if (!response.ok) {
      throw new Error("Falha ao buscar produto")
    }

    return await response.json()
  } catch (error) {
    console.error(`Erro ao obter produto ${id}:`, error)
    return null
  }
}

export async function criarProduto(
  produto: Omit<Produto, "id">,
): Promise<{ success: boolean; message: string; produto?: Produto }> {
  try {
    const response = await fetch("http://sua-api.com/produtos", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(produto),
    })

    const data = await response.json()

    if (!response.ok) {
      return { success: false, message: data.message || "Falha ao criar produto" }
    }

    return { success: true, message: "Produto criado com sucesso", produto: data }
  } catch (error) {
    console.error("Erro ao criar produto:", error)
    return { success: false, message: "Erro ao criar produto" }
  }
}

export async function atualizarProduto(
  id: string,
  produto: Partial<Produto>,
): Promise<{ success: boolean; message: string }> {
  try {
    const response = await fetch(`http://sua-api.com/produtos/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(produto),
    })

    const data = await response.json()

    if (!response.ok) {
      return { success: false, message: data.message || "Falha ao atualizar produto" }
    }

    return { success: true, message: "Produto atualizado com sucesso" }
  } catch (error) {
    console.error(`Erro ao atualizar produto ${id}:`, error)
    return { success: false, message: "Erro ao atualizar produto" }
  }
}

export async function excluirProduto(id: string): Promise<{ success: boolean; message: string }> {
  try {
    const response = await fetch(`http://sua-api.com/produtos/${id}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
    })

    if (!response.ok) {
      const data = await response.json()
      return { success: false, message: data.message || "Falha ao excluir produto" }
    }

    return { success: true, message: "Produto excluído com sucesso" }
  } catch (error) {
    console.error(`Erro ao excluir produto ${id}:`, error)
    return { success: false, message: "Erro ao excluir produto" }
  }
}
