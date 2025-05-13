"use client"

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Checkbox } from "@/components/ui/checkbox"
import { ShoppingCart, Plus, Minus, PlusCircle } from "lucide-react"
import { obterListaCompras } from "@/lib/api"
import type { Produto } from "@/lib/types"
import { useState } from "react"
import { useChecklist } from "@/lib/checklist-context"

export default async function ListaCompras({ consulta }: { consulta: string }) {
  // Em um app real, isso buscaria da sua API
  const produtos = await obterListaCompras(consulta)

  return (
    <Card>
      <CardHeader className="pb-3 border-b">
        <CardTitle className="text-lg font-medium flex items-center">
          <ShoppingCart className="h-5 w-5 mr-2 text-gray-500" />
          Lista de Compras
        </CardTitle>
      </CardHeader>
      <CardContent className="pt-4">
        <ListaComprasCliente produtosIniciais={produtos} />
      </CardContent>
    </Card>
  )
}

// Componente cliente para lidar com interatividade
function ListaComprasCliente({ produtosIniciais }: { produtosIniciais: Produto[] }) {
  const [produtos, setProdutos] = useState(
    produtosIniciais.map((p) => ({
      ...p,
      quantidadeSelecionada: 1,
      selecionado: false,
    })),
  )

  const { addItem, isInChecklist } = useChecklist()

  const atualizarQuantidade = (id: string, delta: number) => {
    setProdutos(
      produtos.map((p) =>
        p.id === id ? { ...p, quantidadeSelecionada: Math.max(1, p.quantidadeSelecionada + delta) } : p,
      ),
    )
  }

  const alternarSelecionado = (id: string) => {
    setProdutos(produtos.map((p) => (p.id === id ? { ...p, selecionado: !p.selecionado } : p)))
  }

  const precoTotal = produtos.reduce((soma, p) => soma + p.preco * p.quantidadeSelecionada, 0)

  // Função para adicionar um produto ao checklist
  const adicionarAoChecklist = (produto: Produto) => {
    addItem(produto)
  }

  return (
    <div className="space-y-4">
      {produtos.length === 0 ? (
        <p className="text-center text-gray-500 py-4">Nenhum item na lista de compras</p>
      ) : (
        <>
          <ul className="space-y-3">
            {produtos.map((produto) => (
              <li key={produto.id} className="flex items-center space-x-3">
                <Checkbox
                  id={`produto-${produto.id}`}
                  checked={produto.selecionado}
                  onCheckedChange={() => alternarSelecionado(produto.id)}
                />
                <div className="flex-grow">
                  <label
                    htmlFor={`produto-${produto.id}`}
                    className={`font-medium ${produto.selecionado ? "line-through text-gray-400" : ""}`}
                  >
                    {produto.nome}
                  </label>
                  <div className="flex items-center text-sm text-gray-500">
                    <span>R$ {produto.preco.toFixed(2)}</span>
                    <span className="mx-2">•</span>
                    <span>{produto.marca}</span>
                  </div>
                </div>
                <div className="flex items-center space-x-1">
                  <Button
                    variant="outline"
                    size="icon"
                    className="h-6 w-6"
                    onClick={() => atualizarQuantidade(produto.id, -1)}
                  >
                    <Minus className="h-3 w-3" />
                  </Button>
                  <span className="w-6 text-center">{produto.quantidadeSelecionada}</span>
                  <Button
                    variant="outline"
                    size="icon"
                    className="h-6 w-6"
                    onClick={() => atualizarQuantidade(produto.id, 1)}
                  >
                    <Plus className="h-3 w-3" />
                  </Button>
                </div>
                <Button
                  variant="ghost"
                  size="icon"
                  className="h-8 w-8 text-purple-600 hover:text-purple-800 hover:bg-purple-50"
                  onClick={() => adicionarAoChecklist(produto)}
                  disabled={isInChecklist(produto.id)}
                  title={isInChecklist(produto.id) ? "Já adicionado ao checklist" : "Adicionar ao checklist"}
                >
                  <PlusCircle className="h-5 w-5" />
                </Button>
              </li>
            ))}
          </ul>

          <div className="pt-4 border-t">
            <div className="flex justify-between mb-4">
              <span className="font-medium">Total:</span>
              <span className="font-bold">R$ {precoTotal.toFixed(2)}</span>
            </div>
            <Button className="w-full bg-purple-600 hover:bg-purple-700">
              <ShoppingCart className="h-4 w-4 mr-2" />
              Adicionar Tudo ao Carrinho
            </Button>
          </div>
        </>
      )}
    </div>
  )
}
