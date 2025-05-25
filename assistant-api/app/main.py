import uvicorn
from fastapi import FastAPI
from routes import facade_route

app = FastAPI(title="API de Compras por Receita")

app.include_router(facade_route.router, prefix="/llm")

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)