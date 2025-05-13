"use client"

import { Card, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { obterResultadosReceitas } from "@/lib/api"
import type { Receita } from "@/lib/types"
import Image from "next/image"
import { useChecklist } from "@/lib/checklist-context"

export default async function RecipeResults({ query }: { query: string }) {
  // Em um app real, isso buscaria da sua API
  const results = await obterResultadosReceitas(query)

  if (results.length === 0) {
    return (
      <Card className="p-6">
        <p className="text-center text-gray-500">Nenhuma receita encontrada para "{query}"</p>
      </Card>
    )
  }

  return (
    <div className="space-y-6">
      {results.map((recipe) => (
        <RecipeCard key={recipe.id} recipe={recipe} />
      ))}
    </div>
  )
}

function RecipeCard({ recipe }: { recipe: Receita }) {
  const { addItem } = useChecklist()

  // Função para adicionar os ingredientes da receita ao checklist
  const addIngredientsToChecklist = () => {
    // Em um app real, você buscaria os ingredientes da receita
    // Para este exemplo, vamos simular alguns ingredientes
    const ingredients = [
      {
        id: `ing-${recipe.id}-1`,
        nome: `Ingrediente 1 para ${recipe.titulo}`,
        categoria: "Diversos",
        marca: "Marca A",
        preco: 5.99,
        quantidade: "500g",
      },
      {
        id: `ing-${recipe.id}-2`,
        nome: `Ingrediente 2 para ${recipe.titulo}`,
        categoria: "Diversos",
        marca: "Marca B",
        preco: 3.49,
        quantidade: "250g",
      },
      {
        id: `ing-${recipe.id}-3`,
        nome: `Ingrediente 3 para ${recipe.titulo}`,
        categoria: "Diversos",
        marca: "Marca C",
        preco: 7.99,
        quantidade: "1kg",
      },
    ]

    // Adicionar cada ingrediente ao checklist
    ingredients.forEach((ingredient) => addItem(ingredient))
  }

  return (
    <Card className="overflow-hidden">
      <div className="md:flex">
        <div className="md:w-1/3 relative h-48 md:h-auto">
          <Image
            src={recipe.imagem || "/placeholder.svg?height=300&width=300"}
            alt={recipe.titulo}
            fill
            className="object-cover"
          />
        </div>
        <div className="md:w-2/3 p-6">
          <CardTitle className="text-xl mb-2">{recipe.titulo}</CardTitle>
          <p className="text-sm text-gray-500 mb-4">
            {recipe.tempoPreparo} • {recipe.porcoes} porções • {recipe.dificuldade}
          </p>
          <p className="text-gray-700 mb-4">{recipe.descricao}</p>
          <div className="flex space-x-2">
            <Button variant="outline" size="sm">
              Ver Receita
            </Button>
            <Button size="sm" className="bg-purple-600 hover:bg-purple-700" onClick={addIngredientsToChecklist}>
              Adicionar Tudo ao Checklist
            </Button>
          </div>
        </div>
      </div>
    </Card>
  )
}
