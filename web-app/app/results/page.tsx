import { Suspense } from "react"
import { Loader2 } from "lucide-react"
import RecipeResults from "@/components/recipe-results"
import ShoppingList from "@/components/shopping-list"
import BackButton from "@/components/back-button"

export default function ResultsPage({
  searchParams,
}: {
  searchParams: { q: string }
}) {
  const query = searchParams.q || ""

  return (
    <main className="container mx-auto px-4 py-8">
      <BackButton />

      <h1 className="text-2xl font-bold mt-4 mb-6">Resultados para: {query}</h1>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2">
          <Suspense
            fallback={
              <div className="flex justify-center py-10">
                <Loader2 className="h-8 w-8 animate-spin" />
              </div>
            }
          >
            <RecipeResults query={query} />
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
            <ShoppingList query={query} />
          </Suspense>
        </div>
      </div>
    </main>
  )
}
