FROM python:3.12-slim

WORKDIR /code

COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

# Copia o código após instalar as dependências
COPY . .

# fastapi dev será executado pelo docker-compose