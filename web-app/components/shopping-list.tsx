"use client"

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Checkbox } from "@/components/ui/checkbox"
import { ShoppingCart, Plus, Minus, PlusCircle } from "lucide-react"
import { obterListaCompras } from "@/lib/api"
import type { Produto } from "@/lib/types"
import { useState } from "react"
import { useChecklist } from "@/lib/checklist-context"

export default async function ShoppingList({ query }: { query: string }) {
  // Em um app real, isso buscaria da sua API
  const products = await obterListaCompras(query)

  return (
    <Card>
      <CardHeader className="pb-3 border-b">
        <CardTitle className="text-lg font-medium flex items-center">
          <ShoppingCart className="h-5 w-5 mr-2 text-gray-500" />
          Lista de Compras
        </CardTitle>
      </CardHeader>
      <CardContent className="pt-4">
        <ShoppingListClient initialProducts={products} />
      </CardContent>
    </Card>
  )
}

// Componente cliente para lidar com interatividade
function ShoppingListClient({ initialProducts }: { initialProducts: Produto[] }) {
  const [products, setProducts] = useState(
    initialProducts.map((p) => ({
      ...p,
      quantity: 1,
      selected: false,
    })),
  )
  const { addItem, isInChecklist } = useChecklist()

  const updateQuantity = (id: string, delta: number) => {
    setProducts(products.map((p) => (p.id === id ? { ...p, quantity: Math.max(1, p.quantity + delta) } : p)))
  }

  const toggleSelected = (id: string) => {
    setProducts(products.map((p) => (p.id === id ? { ...p, selected: !p.selected } : p)))
  }

  const totalPrice = products.reduce((sum, p) => sum + p.preco * p.quantity, 0)

  // Função para adicionar um produto ao checklist
  const addToChecklist = (product: Produto) => {
    addItem({
      id: product.id,
      nome: product.nome,
      categoria: product.categoria,
      marca: product.marca,
      preco: product.preco,
      quantidade: product.quantidade,
    })
  }

  return (
    <div className="space-y-4">
      {products.length === 0 ? (
        <p className="text-center text-gray-500 py-4">Nenhum item na lista de compras</p>
      ) : (
        <>
          <ul className="space-y-3">
            {products.map((product) => (
              <li key={product.id} className="flex items-center space-x-3">
                <Checkbox
                  id={`product-${product.id}`}
                  checked={product.selected}
                  onCheckedChange={() => toggleSelected(product.id)}
                />
                <div className="flex-grow">
                  <label
                    htmlFor={`product-${product.id}`}
                    className={`font-medium ${product.selected ? "line-through text-gray-400" : ""}`}
                  >
                    {product.nome}
                  </label>
                  <div className="flex items-center text-sm text-gray-500">
                    <span>R$ {product.preco.toFixed(2)}</span>
                    <span className="mx-2">•</span>
                    <span>{product.marca}</span>
                  </div>
                </div>
                <div className="flex items-center space-x-1">
                  <Button
                    variant="outline"
                    size="icon"
                    className="h-6 w-6"
                    onClick={() => updateQuantity(product.id, -1)}
                  >
                    <Minus className="h-3 w-3" />
                  </Button>
                  <span className="w-6 text-center">{product.quantity}</span>
                  <Button
                    variant="outline"
                    size="icon"
                    className="h-6 w-6"
                    onClick={() => updateQuantity(product.id, 1)}
                  >
                    <Plus className="h-3 w-3" />
                  </Button>
                </div>
                <Button
                  variant="ghost"
                  size="icon"
                  className="h-8 w-8 text-purple-600 hover:text-purple-800 hover:bg-purple-50"
                  onClick={() => addToChecklist(product)}
                  disabled={isInChecklist(product.id)}
                  title={isInChecklist(product.id) ? "Já adicionado ao checklist" : "Adicionar ao checklist"}
                >
                  <PlusCircle className="h-5 w-5" />
                </Button>
              </li>
            ))}
          </ul>

          <div className="pt-4 border-t">
            <div className="flex justify-between mb-4">
              <span className="font-medium">Total:</span>
              <span className="font-bold">R$ {totalPrice.toFixed(2)}</span>
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
