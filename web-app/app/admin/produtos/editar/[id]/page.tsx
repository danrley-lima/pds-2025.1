import { Suspense } from "react"
import { Loader2 } from "lucide-react"
import FormularioProduto from "@/components/admin/formulario-produto"
import BotaoVoltar from "@/components/botao-voltar"
import { obterProduto } from "@/lib/api"

export default async function PaginaEditarProduto({
  params,
}: {
  params: { id: string }
}) {
  const produto = await obterProduto(params.id)

  return (
    <main className="container mx-auto px-4 py-8">
      <BotaoVoltar />

      <h1 className="text-2xl font-bold mt-4 mb-6">Editar Produto</h1>

      <Suspense
        fallback={
          <div className="flex justify-center py-10">
            <Loader2 className="h-8 w-8 animate-spin" />
          </div>
        }
      >
        {produto ? (
          <FormularioProduto produtoExistente={produto} />
        ) : (
          <div className="text-center py-10">
            <p className="text-lg text-gray-500">Produto não encontrado</p>
          </div>
        )}
      </Suspense>
    </main>
  )
}
