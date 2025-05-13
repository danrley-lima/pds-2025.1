import { Suspense } from "react"
import AssistenteCompras from "@/components/assistente-compras"
import PesquisasRecentes from "@/components/pesquisas-recentes"
import ReceitasPopulares from "@/components/receitas-populares"
import { Loader2 } from "lucide-react"
import { promotionsApi } from "@/lib/api"

export default function Home() {
  var list = promotionsApi.listar;
    
  console.log("Passei aqui");
  console.log(list);
  return (
    <main className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-2">Assistente Inteligente de Compras</h1>
      <p className="text-gray-500 mb-8">
        Peça receitas ou ideias de refeições e nós ajudaremos a encontrar os ingredientes
      </p>

      <Suspense
        fallback={
          <div className="flex justify-center py-10">
            <Loader2 className="h-8 w-8 animate-spin" />
          </div>
        }
      >
        <AssistenteCompras />
      </Suspense>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mt-12">
        <PesquisasRecentes />
        <ReceitasPopulares />
      </div>
    </main>
  )
}
