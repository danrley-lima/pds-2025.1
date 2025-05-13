export interface Receita {
  id: string
  titulo: string
  descricao: string
  imagem: string
  tempoPreparo: string
  porcoes: number
  dificuldade: string
}

export interface Produto {
  id: string
  nome: string
  categoria: string
  marca: string
  preco: number
  quantidade: string
}

export interface Promocao {
  id: string
  descricao: string
  nomeDoProduto: string
  precoOriginal: number
  precoPromocional: number
  dataInicial: Date
  dataFinal: Date
}