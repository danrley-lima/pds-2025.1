"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Checkbox } from "@/components/ui/checkbox"
import { CheckSquare, Trash2, ShoppingCart, Loader2 } from "lucide-react"
import { useToast } from "@/components/ui/use-toast"
import type { Produto } from "@/lib/types"
import { Badge } from "@/components/ui/badge"
import Link from "next/link"

// Tipo para os itens do checklist com estado de checked
interface ChecklistItem extends Produto {
  checked: boolean
}

export default function ChecklistContainer() {
  const [items, setItems] = useState<ChecklistItem[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const { toast } = useToast()

  // Carregar itens do localStorage ao montar o componente
  useEffect(() => {
    const loadItems = () => {
      try {
        const savedItems = localStorage.getItem("checklist")
        if (savedItems) {
          setItems(JSON.parse(savedItems))
        }
      } catch (error) {
        console.error("Erro ao carregar checklist:", error)
        toast({
          title: "Erro",
          description: "Não foi possível carregar seu checklist",
          variant: "destructive",
        })
      } finally {
        setIsLoading(false)
      }
    }

    loadItems()
  }, [toast])

  // Salvar itens no localStorage quando mudam
  useEffect(() => {
    if (!isLoading) {
      localStorage.setItem("checklist", JSON.stringify(items))
    }
  }, [items, isLoading])

  // Alternar o estado de checked de um item
  const toggleChecked = (id: string) => {
    setItems(items.map((item) => (item.id === id ? { ...item, checked: !item.checked } : item)))
  }

  // Remover um item do checklist
  const removeItem = (id: string) => {
    setItems(items.filter((item) => item.id !== id))
    toast({
      title: "Item removido",
      description: "Item removido do checklist",
    })
  }

  // Limpar todos os itens marcados como checked
  const clearCheckedItems = () => {
    setItems(items.filter((item) => !item.checked))
    toast({
      title: "Itens concluídos removidos",
      description: "Os itens marcados foram removidos do checklist",
    })
  }

  // Limpar todo o checklist
  const clearAllItems = () => {
    setItems([])
    toast({
      title: "Checklist limpo",
      description: "Todos os itens foram removidos do checklist",
    })
  }

  if (isLoading) {
    return (
      <div className="flex justify-center py-10">
        <Loader2 className="h-8 w-8 animate-spin" />
      </div>
    )
  }

  const checkedCount = items.filter((item) => item.checked).length
  const totalCount = items.length

  return (
    <Card>
      <CardHeader className="pb-3 border-b">
        <div className="flex justify-between items-center">
          <CardTitle className="text-lg font-medium flex items-center">
            <CheckSquare className="h-5 w-5 mr-2 text-purple-500" />
            Checklist de Compras
          </CardTitle>
          <Badge variant="outline" className="ml-2">
            {checkedCount}/{totalCount} concluídos
          </Badge>
        </div>
      </CardHeader>
      <CardContent className="pt-4">
        {items.length === 0 ? (
          <div className="text-center py-8">
            <p className="text-gray-500 mb-4">Seu checklist está vazio</p>
            <Link href="/">
              <Button className="bg-purple-600 hover:bg-purple-700">
                <ShoppingCart className="h-4 w-4 mr-2" />
                Buscar Produtos
              </Button>
            </Link>
          </div>
        ) : (
          <div className="space-y-4">
            <ul className="space-y-3">
              {items.map((item) => (
                <li key={item.id} className="flex items-center space-x-3 py-2 border-b border-gray-100 last:border-0">
                  <Checkbox
                    id={`check-${item.id}`}
                    checked={item.checked}
                    onCheckedChange={() => toggleChecked(item.id)}
                  />
                  <div className="flex-grow">
                    <label
                      htmlFor={`check-${item.id}`}
                      className={`font-medium ${item.checked ? "line-through text-gray-400" : ""}`}
                    >
                      {item.nome}
                    </label>
                    <div className="flex items-center text-sm text-gray-500">
                      <span>R$ {item.preco.toFixed(2)}</span>
                      <span className="mx-2">•</span>
                      <span>{item.quantidade}</span>
                    </div>
                  </div>
                  <Button
                    variant="ghost"
                    size="sm"
                    className="text-red-500 hover:text-red-700 hover:bg-red-50"
                    onClick={() => removeItem(item.id)}
                  >
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </li>
              ))}
            </ul>

            <div className="flex justify-between pt-4">
              <Button variant="outline" size="sm" onClick={clearCheckedItems} disabled={checkedCount === 0}>
                Remover Concluídos
              </Button>
              <Button
                variant="outline"
                size="sm"
                className="text-red-500 hover:text-red-700 hover:border-red-300"
                onClick={clearAllItems}
              >
                Limpar Tudo
              </Button>
            </div>
          </div>
        )}
      </CardContent>
    </Card>
  )
}
