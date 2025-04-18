from app.database.database import Base, SessionLocal, engine
from app.models.models import Produto

# Garante que as tabelas existam
Base.metadata.create_all(bind=engine)

# Inicia sessão
db = SessionLocal()

# Lista de produtos de exemplo
produtos = [
    Produto(
        nome="Molho de Tomate Tradicional Predilecta",
        categoria="Molhos",
        marca="Predilecta",
        preco=5.49,
        quantidade="340g",
    ),
    Produto(
        nome="Passata de Tomate Heinz",
        categoria="Molhos",
        marca="Heinz",
        preco=7.99,
        quantidade="500ml",
    ),
    Produto(
        nome="Massa para Lasanha Dona Benta",
        categoria="Massas",
        marca="Dona Benta",
        preco=4.50,
        quantidade="500g",
    ),
    Produto(
        nome="Lasanha Barilla",
        categoria="Massas",
        marca="Barilla",
        preco=6.20,
        quantidade="500g",
    ),
    Produto(
        nome="Queijo Mussarela Ralada",
        categoria="Queijos",
        marca="Polenghi",
        preco=12.00,
        quantidade="200g",
    ),
    Produto(
        nome="Carne Moída de Patinho",
        categoria="Carnes e proteínas",
        marca="Friboi",
        preco=25.00,
        quantidade="1kg",
    ),
    # adicione quantos outros quiser...
]

# Insere todos
db.add_all(produtos)
db.commit()
db.close()

print(f"{len(produtos)} produtos inseridos no banco.")
