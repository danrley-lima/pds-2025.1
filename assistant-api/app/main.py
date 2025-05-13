import uvicorn
from fastapi import FastAPI
from routers import receitas, produtos, promotions

app = FastAPI(title="API de Compras por Receita")

app.include_router(receitas.router, prefix="/api")
app.include_router(produtos.router, prefix="/api")
app.include_router(promotions.router, prefix="/api")

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)