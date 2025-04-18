import { Suspense } from "react"
import { Loader2, Plus } from "lucide-react"
import { Button } from "@/components/ui/button"
import TabelaProdutos from "@/components/admin/tabela-produtos"
import Link from "next/link"

export default function PaginaAdminProdutos() {
  return (
    <main className="container mx-auto px-4 py-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Gerenciamento de Produtos</h1>
        <Link href="/admin/produtos/novo">
          <Button className="bg-purple-600 hover:bg-purple-700">
            <Plus className="h-4 w-4 mr-2" />
            Novo Produto
          </Button>
        </Link>
      </div>

      <Suspense
        fallback={
          <div className="flex justify-center py-10">
            <Loader2 className="h-8 w-8 animate-spin" />
          </div>
        }
      >
        <TabelaProdutos />
      </Suspense>
    </main>
  )
}
