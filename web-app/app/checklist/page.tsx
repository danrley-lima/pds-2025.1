import { Suspense } from "react"
import { Loader2 } from "lucide-react"
import ChecklistContainer from "@/components/checklist-container"

export default function ChecklistPage() {
  return (
    <main className="container mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-6">Meu Checklist de Compras</h1>

      <Suspense
        fallback={
          <div className="flex justify-center py-10">
            <Loader2 className="h-8 w-8 animate-spin" />
          </div>
        }
      >
        <ChecklistContainer />
      </Suspense>
    </main>
  )
}