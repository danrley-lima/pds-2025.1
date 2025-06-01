import 'package:flutter/material.dart';
import '../models/assistant_product_model.dart';

class AiProductCard extends StatelessWidget {
  final AssistantProductModel product;

  const AiProductCard({super.key, required this.product});

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(product.name, style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
            Text("Marca: ${product.brand}"),
            Text("Categoria: ${product.categoryName}"),
            Text("Quantidade requerida: ${product.requiredQuantity}"),
            Text("Preço unitário: R\$ ${product.unitPrice.toStringAsFixed(2)}"),
            if (product.promotionalPrice != null)
              Text(
                "Preço promocional: R\$ ${product.promotionalPrice!.toStringAsFixed(2)}",
                style: const TextStyle(color: Colors.green),
              ),
          ],
        ),
      ),
    );
  }
}