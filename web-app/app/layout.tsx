import type React from "react"
import type { Metadata } from "next"
import { Inter } from "next/font/google"
import "./globals.css"
import { Toaster } from "@/components/ui/toaster"
import NavBar from "@/components/nav-bar"
import { ChecklistProvider } from "@/lib/checklist-context"

const inter = Inter({ subsets: ["latin"] })

export const metadata: Metadata = {
  title: "Assistente de Compras com IA",
  description: "Aplicativo de assistência para compras em supermercado com IA",
    generator: 'v0.dev'
}

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <html lang="pt-BR">
      <body className={inter.className}>
        <ChecklistProvider>
          <NavBar />
          {children}
          <Toaster />
        </ChecklistProvider>
      </body>
    </html>
  )
}
