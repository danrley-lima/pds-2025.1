import { Suspense } from "react"
import { Loader2 } from "lucide-react"
import ResultadosReceitas from "@/components/resultados-receitas"
import ListaCompras from "@/components/lista-compras"
import BotaoVoltar from "@/components/botao-voltar"

export default function PaginaResultados({
  searchParams,
}: {
  searchParams: { q: string }
}) {
  const consulta = searchParams.q || ""

  return (
    <main className="container mx-auto px-4 py-8">
      <BotaoVoltar />

      <h1 className="text-2xl font-bold mt-4 mb-6">Resultados para: {consulta}</h1>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2">
          <Suspense
            fallback={
              <div className="flex justify-center py-10">
                <Loader2 className="h-8 w-8 animate-spin" />
              </div>
            }
          >
            <ResultadosReceitas consulta={consulta} />
          </Suspense>
        </div>

        <div>
          <Suspense
            fallback={
              <div className="flex justify-center py-10">
                <Loader2 className="h-8 w-8 animate-spin" />
              </div>
            }
          >
            <ListaCompras consulta={consulta} />
          </Suspense>
        </div>
      </div>
    </main>
  )
}
