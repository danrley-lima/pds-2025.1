import uvicorn
from fastapi import FastAPI
from app.routes import facade_route

app = FastAPI(title="LLM para recomendações de produtos")

app.include_router(facade_route.router, prefix="/llm")

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)