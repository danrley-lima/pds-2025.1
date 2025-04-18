import { Card, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { obterResultadosReceitas } from "@/lib/api"
import type { Receita } from "@/lib/types"
import Image from "next/image"

export default async function ResultadosReceitas({ consulta }: { consulta: string }) {
  // Em um app real, isso buscaria da sua API
  const resultados = await obterResultadosReceitas(consulta)

  if (resultados.length === 0) {
    return (
      <Card className="p-6">
        <p className="text-center text-gray-500">Nenhuma receita encontrada para "{consulta}"</p>
      </Card>
    )
  }

  return (
    <div className="space-y-6">
      {resultados.map((receita) => (
        <CardReceita key={receita.id} receita={receita} />
      ))}
    </div>
  )
}

function CardReceita({ receita }: { receita: Receita }) {
  return (
    <Card className="overflow-hidden">
      <div className="md:flex">
        <div className="md:w-1/3 relative h-48 md:h-auto">
          <Image
            src={receita.imagem || "/placeholder.svg?height=300&width=300"}
            alt={receita.titulo}
            fill
            className="object-cover"
          />
        </div>
        <div className="md:w-2/3 p-6">
          <CardTitle className="text-xl mb-2">{receita.titulo}</CardTitle>
          <p className="text-sm text-gray-500 mb-4">
            {receita.tempoPreparo} • {receita.porcoes} porções • {receita.dificuldade}
          </p>
          <p className="text-gray-700 mb-4">{receita.descricao}</p>
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
