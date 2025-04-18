"use client"

import { useState } from "react"
import { useRouter } from "next/navigation"
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import * as z from "zod"
import { Button } from "@/components/ui/button"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { Loader2 } from "lucide-react"
import { useToast } from "@/components/ui/use-toast"
import { criarProduto, atualizarProduto } from "@/lib/api"
import type { Produto } from "@/lib/types"

// Schema de validação
const produtoSchema = z.object({
  nome: z.string().min(3, { message: "Nome deve ter pelo menos 3 caracteres" }),
  categoria: z.string().min(2, { message: "Categoria deve ter pelo menos 2 caracteres" }),
  marca: z.string().min(2, { message: "Marca deve ter pelo menos 2 caracteres" }),
  preco: z.coerce.number().positive({ message: "Preço deve ser um valor positivo" }),
  quantidade: z.string().min(1, { message: "Quantidade é obrigatória" }),
})

type FormValues = z.infer<typeof produtoSchema>

interface FormularioProdutoProps {
  produtoExistente?: Produto
}

export default function FormularioProduto({ produtoExistente }: FormularioProdutoProps) {
  const [enviando, setEnviando] = useState(false)
  const router = useRouter()
  const { toast } = useToast()
  const editando = !!produtoExistente

  // Inicializar o formulário
  const form = useForm<FormValues>({
    resolver: zodResolver(produtoSchema),
    defaultValues: {
      nome: produtoExistente?.nome || "",
      categoria: produtoExistente?.categoria || "",
      marca: produtoExistente?.marca || "",
      preco: produtoExistente?.preco || 0,
      quantidade: produtoExistente?.quantidade || "",
    },
  })

  const onSubmit = async (values: FormValues) => {
    setEnviando(true)

    try {
      let resultado

      if (editando) {
        resultado = await atualizarProduto(produtoExistente.id, values)
      } else {
        resultado = await criarProduto(values)
      }

      if (resultado.success) {
        toast({
          title: editando ? "Produto atualizado" : "Produto criado",
          description: resultado.message,
          variant: "default",
        })

        // Redirecionar para a lista de produtos
        router.push("/admin/produtos")
        router.refresh()
      } else {
        toast({
          title: "Erro",
          description: resultado.message,
          variant: "destructive",
        })
      }
    } catch (error) {
      console.error("Erro ao salvar produto:", error)
      toast({
        title: "Erro",
        description: "Ocorreu um erro ao salvar o produto. Tente novamente.",
        variant: "destructive",
      })
    } finally {
      setEnviando(false)
    }
  }

  return (
    <div className="max-w-2xl mx-auto">
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
          <FormField
            control={form.control}
            name="nome"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Nome do Produto</FormLabel>
                <FormControl>
                  <Input placeholder="Digite o nome do produto" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="categoria"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Categoria</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: Laticínios, Bebidas, etc." {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="marca"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Marca</FormLabel>
                  <FormControl>
                    <Input placeholder="Digite a marca do produto" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="preco"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Preço (R$)</FormLabel>
                  <FormControl>
                    <Input type="number" step="0.01" placeholder="0.00" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="quantidade"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Quantidade</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: 500g, 1kg, 1un" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <div className="flex justify-end space-x-4 pt-4">
            <Button type="button" variant="outline" onClick={() => router.push("/admin/produtos")} disabled={enviando}>
              Cancelar
            </Button>
            <Button type="submit" className="bg-purple-600 hover:bg-purple-700" disabled={enviando}>
              {enviando ? (
                <>
                  <Loader2 className="h-4 w-4 mr-2 animate-spin" />
                  {editando ? "Salvando..." : "Criando..."}
                </>
              ) : editando ? (
                "Salvar Alterações"
              ) : (
                "Criar Produto"
              )}
            </Button>
          </div>
        </form>
      </Form>
    </div>
  )
}
