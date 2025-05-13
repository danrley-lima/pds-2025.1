"use client"

import { createContext, useContext, useState, useEffect, type ReactNode } from "react"
import type { Produto } from "@/lib/types"

// Tipo para os itens do checklist com estado de checked
export interface ChecklistItem extends Produto {
  checked: boolean
}

interface ChecklistContextType {
  items: ChecklistItem[]
  addItem: (produto: Produto) => void
  removeItem: (id: string) => void
  toggleChecked: (id: string) => void
  clearCheckedItems: () => void
  clearAllItems: () => void
  isInChecklist: (id: string) => boolean
}

const ChecklistContext = createContext<ChecklistContextType | undefined>(undefined)

export function ChecklistProvider({ children }: { children: ReactNode }) {
  const [items, setItems] = useState<ChecklistItem[]>([])
  const [isInitialized, setIsInitialized] = useState(false)

  // Carregar itens do localStorage ao montar o componente
  useEffect(() => {
    try {
      const savedItems = localStorage.getItem("checklist")
      if (savedItems) {
        setItems(JSON.parse(savedItems))
      }
    } catch (error) {
      console.error("Erro ao carregar checklist:", error)
    } finally {
      setIsInitialized(true)
    }
  }, [])

  // Salvar itens no localStorage quando mudam
  useEffect(() => {
    if (isInitialized) {
      localStorage.setItem("checklist", JSON.stringify(items))
    }
  }, [items, isInitialized])

  // Adicionar um produto ao checklist
  const addItem = (produto: Produto) => {
    // Verificar se o produto já existe no checklist
    if (!items.some((item) => item.id === produto.id)) {
      setItems([...items, { ...produto, checked: false }])
    }
  }

  // Remover um item do checklist
  const removeItem = (id: string) => {
    setItems(items.filter((item) => item.id !== id))
  }

  // Alternar o estado de checked de um item
  const toggleChecked = (id: string) => {
    setItems(items.map((item) => (item.id === id ? { ...item, checked: !item.checked } : item)))
  }

  // Limpar todos os itens marcados como checked
  const clearCheckedItems = () => {
    setItems(items.filter((item) => !item.checked))
  }

  // Limpar todo o checklist
  const clearAllItems = () => {
    setItems([])
  }

  // Verificar se um produto está no checklist
  const isInChecklist = (id: string) => {
    return items.some((item) => item.id === id)
  }

  return (
    <ChecklistContext.Provider
      value={{
        items,
        addItem,
        removeItem,
        toggleChecked,
        clearCheckedItems,
        clearAllItems,
        isInChecklist,
      }}
    >
      {children}
    </ChecklistContext.Provider>
  )
}

export function useChecklist() {
  const context = useContext(ChecklistContext)
  if (context === undefined) {
    throw new Error("useChecklist must be used within a ChecklistProvider")
  }
  return context
}
