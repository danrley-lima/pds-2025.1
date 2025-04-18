import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { TrendingUp, ArrowRight } from "lucide-react"
import Link from "next/link"

// Em um app real, isso viria de uma API
const popularRecipes = [
  { id: 1, name: "Macarrão de frango de uma panela só", difficulty: "Fácil", time: "30 mins" },
  { id: 2, name: "Chili vegetariano", difficulty: "Médio", time: "45 mins" },
  { id: 3, name: "Bolo de banana rápido", difficulty: "Fácil", time: "1 hora" },
]

export default function PopularRecipes() {
  return (
    <Card>
      <CardHeader className="pb-3">
        <CardTitle className="text-lg font-medium flex items-center">
          <TrendingUp className="h-5 w-5 mr-2 text-gray-500" />
          Receitas Populares
        </CardTitle>
      </CardHeader>
      <CardContent>
        <ul className="space-y-3">
          {popularRecipes.map((recipe) => (
            <li key={recipe.id} className="flex justify-between items-center">
              <div>
                <p className="font-medium">{recipe.name}</p>
                <p className="text-sm text-gray-500">
                  {recipe.difficulty} • {recipe.time}
                </p>
              </div>
              <Link href={`/results?q=${encodeURIComponent(recipe.name)}`}>
                <Button variant="ghost" size="sm">
                  <ArrowRight className="h-4 w-4" />
                </Button>
              </Link>
            </li>
          ))}
        </ul>
      </CardContent>
    </Card>
  )
}
