import FormularioProduto from "@/components/admin/formulario-produto"
import BotaoVoltar from "@/components/botao-voltar"

export default function PaginaNovoProduto() {
  return (
    <main className="container mx-auto px-4 py-8">
      <BotaoVoltar />

      <h1 className="text-2xl font-bold mt-4 mb-6">Cadastrar Novo Produto</h1>

      <FormularioProduto />
    </main>
  )
}
