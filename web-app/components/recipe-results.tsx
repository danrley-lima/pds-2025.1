import { Card, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { getRecipeResults } from "@/lib/api"
import type { Recipe } from "@/lib/types"
import Image from "next/image"

export default async function RecipeResults({ query }: { query: string }) {
  // Em um app real, isso buscaria da sua API
  const results = await getRecipeResults(query)

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

function RecipeCard({ recipe }: { recipe: Recipe }) {
  return (
    <Card className="overflow-hidden">
      <div className="md:flex">
        <div className="md:w-1/3 relative h-48 md:h-auto">
          <Image
            src={recipe.image || "/placeholder.svg?height=300&width=300"}
            alt={recipe.title}
            fill
            className="object-cover"
          />
        </div>
        <div className="md:w-2/3 p-6">
          <CardTitle className="text-xl mb-2">{recipe.title}</CardTitle>
          <p className="text-sm text-gray-500 mb-4">
            {recipe.prepTime} • {recipe.servings} porções • {recipe.difficulty}
          </p>
          <p className="text-gray-700 mb-4">{recipe.description}</p>
          <div className="flex space-x-2">
            <Button variant="outline" size="sm">
              Ver Receita
            </Button>
            <Button size="sm" className="bg-purple-600 hover:bg-purple-700">
              Adicionar Tudo ao Carrinho
            </Button>
          </div>
        </div>
      </div>
    </Card>
  )
}
