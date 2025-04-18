"use client"

import type React from "react"

import { useState } from "react"
import { useRouter } from "next/navigation"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Card } from "@/components/ui/card"
import { Search, ShoppingCart, Sparkles, Loader2 } from "lucide-react"
import { processarSolicitacaoCompra } from "@/app/actions"

export default function AssistenteCompras() {
  const [consulta, setConsulta] = useState("")
  const [carregando, setCarregando] = useState(false)
  const router = useRouter()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!consulta.trim()) return

    setCarregando(true)
    try {
      // Processa a solicitação e redireciona para a página de resultados
      await processarSolicitacaoCompra(consulta)
      router.push(`/resultados?q=${encodeURIComponent(consulta)}`)
    } catch (error) {
      console.error("Erro ao processar solicitação:", error)
    } finally {
      setCarregando(false)
    }
  }

  return (
    <Card className="p-6 shadow-md">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="flex items-center space-x-2 mb-4">
          <Sparkles className="h-5 w-5 text-purple-500" />
          <h2 className="text-xl font-semibold">Assistente de Compras com IA</h2>
        </div>

        <p className="text-sm text-gray-500 mb-4">
          Peça receitas, ideias de refeições ou ingredientes específicos. Por exemplo:
        </p>

        <div className="flex flex-col space-y-2">
          <Button
            variant="outline"
            className="justify-start text-gray-600 hover:text-purple-600 hover:border-purple-300"
            onClick={() => setConsulta("Receita de lasanha para 10 pessoas")}
            type="button"
          >
            Receita de lasanha para 10 pessoas
          </Button>
          <Button
            variant="outline"
            className="justify-start text-gray-600 hover:text-purple-600 hover:border-purple-300"
            onClick={() => setConsulta("Ingredientes para um café da manhã saudável para 4 pessoas")}
            type="button"
          >
            Ingredientes para um café da manhã saudável para 4 pessoas
          </Button>
          <Button
            variant="outline"
            className="justify-start text-gray-600 hover:text-purple-600 hover:border-purple-300"
            onClick={() => setConsulta("Ideias de jantar sem glúten")}
            type="button"
          >
            Ideias de jantar sem glúten
          </Button>
        </div>

        <div className="flex space-x-2">
          <div className="relative flex-grow">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
            <Input
              value={consulta}
              onChange={(e) => setConsulta(e.target.value)}
              placeholder="O que você gostaria de cozinhar?"
              className="pl-10"
            />
          </div>
          <Button type="submit" disabled={carregando} className="bg-purple-600 hover:bg-purple-700">
            {carregando ? <Loader2 className="h-4 w-4 animate-spin mr-2" /> : <ShoppingCart className="h-4 w-4 mr-2" />}
            Encontrar Ingredientes
          </Button>
        </div>
      </form>
    </Card>
  )
}
