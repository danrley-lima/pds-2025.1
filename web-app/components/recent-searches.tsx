import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Clock, ArrowRight } from "lucide-react"
import Link from "next/link"

// Em um app real, isso viria de um banco de dados ou armazenamento local
const recentSearches = [
  { id: 1, query: "Espaguete à bolonhesa para 4", timestamp: "2 horas atrás" },
  { id: 2, query: "Ideias de café da manhã vegano", timestamp: "Ontem" },
  { id: 3, query: "Receita de bolo sem glúten", timestamp: "3 dias atrás" },
]

export default function RecentSearches() {
  return (
    <Card>
      <CardHeader className="pb-3">
        <CardTitle className="text-lg font-medium flex items-center">
          <Clock className="h-5 w-5 mr-2 text-gray-500" />
          Pesquisas Recentes
        </CardTitle>
      </CardHeader>
      <CardContent>
        <ul className="space-y-3">
          {recentSearches.map((search) => (
            <li key={search.id} className="flex justify-between items-center">
              <div>
                <p className="font-medium">{search.query}</p>
                <p className="text-sm text-gray-500">{search.timestamp}</p>
              </div>
              <Link href={`/results?q=${encodeURIComponent(search.query)}`}>
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
