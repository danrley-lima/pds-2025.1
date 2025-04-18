"use client"

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Checkbox } from "@/components/ui/checkbox"
import { ShoppingCart, Plus, Minus } from "lucide-react"
import { getShoppingList } from "@/lib/api"
import type { Product } from "@/lib/types"
import { useState } from "react"

export default async function ShoppingList({ query }: { query: string }) {
  // Em um app real, isso buscaria da sua API
  const products = await getShoppingList(query)

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
function ShoppingListClient({ initialProducts }: { initialProducts: Product[] }) {
  const [products, setProducts] = useState(
    initialProducts.map((p) => ({
      ...p,
      quantity: 1,
      selected: false,
    })),
  )

  const updateQuantity = (id: string, delta: number) => {
    setProducts(products.map((p) => (p.id === id ? { ...p, quantity: Math.max(1, p.quantity + delta) } : p)))
  }

  const toggleSelected = (id: string) => {
    setProducts(products.map((p) => (p.id === id ? { ...p, selected: !p.selected } : p)))
  }

  const totalPrice = products.reduce((sum, p) => sum + p.price * p.quantity, 0)

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
                    {product.name}
                  </label>
                  <div className="flex items-center text-sm text-gray-500">
                    <span>R$ {product.price.toFixed(2)}</span>
                    <span className="mx-2">•</span>
                    <span>{product.brand}</span>
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
