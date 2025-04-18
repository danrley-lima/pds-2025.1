"use client"

import { useState, useEffect } from "react"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Button } from "@/components/ui/button"
import { Pencil, Trash2, AlertCircle } from "lucide-react"
import { Loader2 } from "lucide-react"
import { listarProdutos, excluirProduto } from "@/lib/api"
import type { Produto } from "@/lib/types"
import Link from "next/link"
import { useToast } from "@/components/ui/use-toast"
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog"

export default function TabelaProdutos() {
  const [produtos, setProdutos] = useState<Produto[]>([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState<string | null>(null)
  const [produtoParaExcluir, setProdutoParaExcluir] = useState<string | null>(null)
  const { toast } = useToast()

  useEffect(() => {
    const carregarProdutos = async () => {
      try {
        setCarregando(true)
        const data = await listarProdutos()
        setProdutos(data)
        setErro(null)
      } catch (error) {
        console.error("Erro ao carregar produtos:", error)
        setErro("Não foi possível carregar a lista de produtos. Tente novamente mais tarde.")
      } finally {
        setCarregando(false)
      }
    }

    carregarProdutos()
  }, [])

  const handleExcluir = async () => {
    if (!produtoParaExcluir) return

    try {
      const resultado = await excluirProduto(produtoParaExcluir)

      if (resultado.success) {
        setProdutos(produtos.filter((p) => p.id !== produtoParaExcluir))
        toast({
          title: "Produto excluído",
          description: "O produto foi excluído com sucesso.",
          variant: "default",
        })
      } else {
        toast({
          title: "Erro ao excluir",
          description: resultado.message,
          variant: "destructive",
        })
      }
    } catch (error) {
      toast({
        title: "Erro ao excluir",
        description: "Ocorreu um erro ao tentar excluir o produto.",
        variant: "destructive",
      })
    } finally {
      setProdutoParaExcluir(null)
    }
  }

  if (carregando) {
    return (
      <div className="flex justify-center py-10">
        <Loader2 className="h-8 w-8 animate-spin" />
      </div>
    )
  }

  if (erro) {
    return (
      <div className="flex flex-col items-center justify-center py-10 text-center">
        <AlertCircle className="h-10 w-10 text-red-500 mb-4" />
        <p className="text-lg font-medium text-gray-900 mb-2">Erro ao carregar produtos</p>
        <p className="text-gray-500">{erro}</p>
        <Button onClick={() => window.location.reload()} variant="outline" className="mt-4">
          Tentar novamente
        </Button>
      </div>
    )
  }

  return (
    <>
      <div className="rounded-md border">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Nome</TableHead>
              <TableHead>Categoria</TableHead>
              <TableHead>Marca</TableHead>
              <TableHead>Preço</TableHead>
              <TableHead>Quantidade</TableHead>
              <TableHead className="text-right">Ações</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {produtos.length === 0 ? (
              <TableRow>
                <TableCell colSpan={6} className="text-center py-10 text-gray-500">
                  Nenhum produto cadastrado
                </TableCell>
              </TableRow>
            ) : (
              produtos.map((produto) => (
                <TableRow key={produto.id}>
                  <TableCell className="font-medium">{produto.nome}</TableCell>
                  <TableCell>{produto.categoria}</TableCell>
                  <TableCell>{produto.marca}</TableCell>
                  <TableCell>R$ {produto.preco.toFixed(2)}</TableCell>
                  <TableCell>{produto.quantidade}</TableCell>
                  <TableCell className="text-right">
                    <div className="flex justify-end space-x-2">
                      <Link href={`/admin/produtos/editar/${produto.id}`}>
                        <Button variant="outline" size="sm">
                          <Pencil className="h-4 w-4" />
                        </Button>
                      </Link>
                      <Button
                        variant="outline"
                        size="sm"
                        className="text-red-500 hover:text-red-700"
                        onClick={() => setProdutoParaExcluir(produto.id)}
                      >
                        <Trash2 className="h-4 w-4" />
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </div>

      <AlertDialog open={!!produtoParaExcluir} onOpenChange={(open) => !open && setProdutoParaExcluir(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Tem certeza?</AlertDialogTitle>
            <AlertDialogDescription>
              Esta ação não pode ser desfeita. Isso excluirá permanentemente o produto do banco de dados.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancelar</AlertDialogCancel>
            <AlertDialogAction onClick={handleExcluir} className="bg-red-500 hover:bg-red-600">
              Excluir
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  )
}
