"use client"

import { Button } from "@/components/ui/button"
import { ArrowLeft } from "lucide-react"
import { useRouter } from "next/navigation"

export default function BotaoVoltar() {
  const router = useRouter()

  return (
    <Button variant="ghost" size="sm" className="pl-0 text-gray-600" onClick={() => router.back()}>
      <ArrowLeft className="h-4 w-4 mr-1" />
      Voltar
    </Button>
  )
}
