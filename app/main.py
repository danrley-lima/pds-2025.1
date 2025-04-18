from fastapi import FastAPI
from .routers import receitas

app = FastAPI(title="API de Compras por Receita")
app.include_router(receitas.router, prefix="/api")
