import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { TrendingUp, ArrowRight } from "lucide-react"
import Link from "next/link"

// Em um app real, isso viria de uma API
const receitasPopulares = [
  { id: 1, nome: "Macarrão de frango de uma panela só", dificuldade: "Fácil", tempo: "30 mins" },
  { id: 2, nome: "Chili vegetariano", dificuldade: "Médio", tempo: "45 mins" },
  { id: 3, nome: "Bolo de banana rápido", dificuldade: "Fácil", tempo: "1 hora" },
]

export default function ReceitasPopulares() {
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
          {receitasPopulares.map((receita) => (
            <li key={receita.id} className="flex justify-between items-center">
              <div>
                <p className="font-medium">{receita.nome}</p>
                <p className="text-sm text-gray-500">
                  {receita.dificuldade} • {receita.tempo}
                </p>
              </div>
              <Link href={`/resultados?q=${encodeURIComponent(receita.nome)}`}>
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
