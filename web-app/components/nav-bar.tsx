"use client"

import Link from "next/link"
import { usePathname } from "next/navigation"
import { cn } from "@/lib/utils"
import { ShoppingCart, Package, Home } from "lucide-react"

export default function NavBar() {
  const pathname = usePathname()

  return (
    <header className="bg-white border-b sticky top-0 z-50">
      <div className="container mx-auto px-4">
        <div className="flex h-16 items-center justify-between">
          <div className="flex items-center">
            <Link href="/" className="flex items-center space-x-2">
              <ShoppingCart className="h-6 w-6 text-purple-600" />
              <span className="text-xl font-bold">SuperMarket IA</span>
            </Link>
          </div>

          <nav className="flex items-center space-x-6">
            <Link
              href="/"
              className={cn(
                "flex items-center text-sm font-medium transition-colors hover:text-purple-600",
                pathname === "/" ? "text-purple-600" : "text-gray-600",
              )}
            >
              <Home className="h-4 w-4 mr-1" />
              Início
            </Link>

            <Link
              href="/admin/produtos"
              className={cn(
                "flex items-center text-sm font-medium transition-colors hover:text-purple-600",
                pathname.startsWith("/admin/produtos") ? "text-purple-600" : "text-gray-600",
              )}
            >
              <Package className="h-4 w-4 mr-1" />
              Gerenciar Produtos
            </Link>
          </nav>
        </div>
      </div>
    </header>
  )
}
